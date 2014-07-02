/**
 * 
 */
package com.shengpay.commons.jmsproxy.interfaceproxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;

import com.shengpay.commons.core.utils.ClassUtils;
import com.shengpay.commons.jmsproxy.bean.Constants;
import com.shengpay.commons.jmsproxy.bean.JmsPropertyAnn;
import com.shengpay.commons.jmsproxy.bean.NativeProcessContext;
import com.shengpay.commons.jmsproxy.jmsconnect.JmsConnect;
import com.shengpay.commons.jmsproxy.utils.JsonUtils;

/**
 * 
 * @copyright	Copyright 2012 SDP Corporation. All rights reserved.
 * @author		lindongcheng <lindongcheng@snda.com>
 * @create		2012-10-25 下午06:03:36
 */

public class InterfaceProxy implements FactoryBean,InvocationHandler{
	
	/**
	 * 系统日志输出句柄
	 */
	private Logger logger = Logger.getLogger(InterfaceProxy.class);
	
	/**
	 * 目标代理接口
	 */
	private Class<?> targetInterface;
	
	/**
	 * JMS连接
	 */
	private JmsConnect jmsConnect;
	
	public InterfaceProxy(Class<?> targetInterface, String connectionFactoryJndi, String destinationJndi) throws JMSException {
		this(targetInterface,new JmsConnect(connectionFactoryJndi, destinationJndi));
	}
	/**
	 * @param targetInterface
	 * @param jmsConnect
	 * @throws JMSException 
	 */
	public InterfaceProxy(Class<?> targetInterface, JmsConnect jmsConnect) throws JMSException {
		this.targetInterface = targetInterface;
		this.jmsConnect = jmsConnect;
		
		jmsConnect.start();
	}

	@Override
	public Object getObject() throws Exception {
		Class<?>[] proxyInterfaces = new Class<?>[] { targetInterface };
		return Proxy.newProxyInstance(proxyInterfaces[0].getClassLoader(), proxyInterfaces, this);
	}
	
	@Override
	public Class<?> getObjectType() {
		return targetInterface;
	}
	
	@Override
	public boolean isSingleton() {
		return true;
	}
	
	/**
	 * 从方法调用信息中获取调用方地址信息
	 * @param method
	 * @param params
	 * @throws JMSException 
	 */
	private String gerLocalHostByMethodCall(Method method, Object[] params) throws JMSException {
		Class<?>[] parameterTypes = method.getParameterTypes();
		for (int i = 0; i < parameterTypes.length; i++) {
			Class<?> class1 = parameterTypes[i];
			Object arg=params[i];
			if(class1 == NativeProcessContext.class && arg!=null) {
				return ((NativeProcessContext)arg).getLocalHost();
			}
		}
		return null;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		// 使用消息生产者发送消息
		String msg = getParamsJsonStr(method, args);
		Map<String, Object> propertiesMap = getPropertyMap(method, args);
		String jMSMessageID=jmsConnect.sendMsg(msg,propertiesMap);
		
		logger.info("成功对方法调用【" + ClassUtils.getMethodCallInfo(method, args) + "】进行了JMS消息发送代理【" + jMSMessageID + "】");
		
		// 如果被调用方法无返回值,则直接返回
		if (method.getReturnType() == void.class) {
			return null;
		}
		
		//读取返回值信息
		Object object = jmsConnect.receiveReturnObj(jMSMessageID, (Class<?>) method.getReturnType());
		if (object instanceof Throwable) {
			throw (Throwable) object;
		} else {
			return object;
		}
	}

	/**
	 * 获取方法调用参数的json字符串信息
	 * @param method
	 * @param args
	 * @return
	 */
	private String getParamsJsonStr(Method method, Object[] args) {
		Object[] newArgs=setNativeProcessContextArg(args,method);
		return JsonUtils.toJson(newArgs);
	}
	
	
	/**
	 * 取得方法参数信息中可以作为JMS消息属性的映射
	 * 
	 * @param method
	 * @param args
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws JMSException
	 */
	private Map<String, Object> getPropertyMap(Method method, Object[] args) throws IllegalArgumentException, IllegalAccessException, JMSException {
		Map<String, Object> propertiesMap = new HashMap<String, Object>();
		
		// 参数级别的属性
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		for (int i = 0; i < parameterAnnotations.length; i++) {
			Annotation[] annotations = parameterAnnotations[i];
			for (int j = 0; j < annotations.length; j++) {
				JmsPropertyAnn jpa = (JmsPropertyAnn) annotations[j];
				propertiesMap.put(jpa.property(), args[i]);
			}
		}
		
		// 参数对象下,域级别的属性
		for (Object arg : args) {
			if (arg == null) {
				continue;
			}
			
			Field[] fields = arg.getClass().getDeclaredFields();
			for (Field field : fields) {
				JmsPropertyAnn jpa = field.getAnnotation(JmsPropertyAnn.class);
				if (jpa == null) {
					continue;
				}
				
				field.setAccessible(true);
				propertiesMap.put(jpa.property(), field.get(arg));
			}
		}
		
		//设置调用方法属性
		String methodSignFull = ClassUtils.getMethodSignFull(method);
		propertiesMap.put(Constants.JMS_PROPERTY_NAME_METHOD_SIGN_FULL, methodSignFull);// 发布刷新文章消息

		//设置调用方信息属性
		String aimHost = gerLocalHostByMethodCall(method, args);
		if(aimHost!=null) {
			propertiesMap.put(Constants.JMS_PROPERTY_NAME_RESPONSE_AIM_HOST, aimHost);
		}
		
		return propertiesMap;
	}
	/**
	 * 在方法调用信息中添加【本地处理上下文】参数信息
	 * @param args
	 * @param method
	 * @return
	 */
	private Object[] setNativeProcessContextArg(Object[] args, Method method) {
		Object[] newArgs=new Object[args.length];
		
		Class<?>[] parameterTypes = method.getParameterTypes();
		for (int i = 0; i < parameterTypes.length; i++) {
			Class<?> class1 = parameterTypes[i];
			if(class1==NativeProcessContext.class && args[i]==null) {
				newArgs[i]=new NativeProcessContext();
			}else {
				newArgs[i]=args[i];
			}
		}
		
		return newArgs;
	}
	
}
