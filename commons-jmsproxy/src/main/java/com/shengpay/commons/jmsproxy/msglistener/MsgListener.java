/**
 * 
 */
package com.shengpay.commons.jmsproxy.msglistener;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.utils.ClassUtils;
import com.shengpay.commons.core.utils.StringUtils;
import com.shengpay.commons.jmsproxy.bean.Constants;
import com.shengpay.commons.jmsproxy.bean.JmsFilterAnn;
import com.shengpay.commons.jmsproxy.bean.NativeProcessAnn;
import com.shengpay.commons.jmsproxy.jmsconnect.JmsConnect;
import com.shengpay.commons.jmsproxy.utils.JsonUtils;

/**
 * 
 * @copyright	Copyright 2012 SDP Corporation. All rights reserved.
 * @author		lindongcheng <lindongcheng@snda.com>
 * @create		2012-10-25 下午05:44:21
 */

public class MsgListener implements MessageListener,BeanClassLoaderAware,BeanFactoryAware {
	
	/**
	 * 附加过滤信息
	 */
	private String filter;
	
	/**
	 * spring的容器(用于创建代理工厂)
	 */
	private BeanFactory beanFactory;
	
	/**
	 * 拦截器列表
	 */
	private String[] interceptorNames;;
	
	/**
	 * 执行信息处理的线程池;
	 */
	private ThreadPoolExecutor threadPoolExecutor;
	
	/**
	 * 执行消息消费的对象(经过了若干层的代理)
	 */
	private Object consumerObject;
	
	/**
	 * spring容器使用的类加载器(用于创建代理工厂)
	 */
	private ClassLoader classLoader;
	
	/**
	 * 被代理的对象
	 */
	private Object targerObject;
	
	/**
	 * 允许被代理的包名前缀
	 */
	private List<String> proxyPackagePrefix;
	
	/**
	 * 方法签名到方法信息的映射
	 */
	private Map<String, Method> methodMap = new HashMap<String, Method>();
	
	/**
	 * JMS连接
	 */
	private JmsConnect jmsConnect;
	
	/**
	 * 系统日志输出句柄
	 */
	private Logger logger = Logger.getLogger(MsgListener.class);
	
	/**
	 * @param targerObject
	 * @param jmsConnect
	 * @throws NamingException 
	 * @throws JMSException 
	 */
	public MsgListener(Object aTargerObject, JmsConnect aJmsConnect) throws JMSException, NamingException {
		targerObject = aTargerObject;
		jmsConnect = aJmsConnect;
		threadPoolExecutor=makeThreadPoolExecutor(10);
		proxyPackagePrefix = getDefaultProxyPackagePrefix();
	}
	
	public MsgListener(Object aTargerObject, String connectionFactoryJndi, String destinationJndi) throws JMSException, NamingException {
		this(aTargerObject,new JmsConnect(connectionFactoryJndi, destinationJndi));
	}
	
	@PostConstruct
	public void init() throws JMSException, NamingException {
		consumerObject = proxyTargetObject(targerObject);

		addListener4Obj(targerObject);
		
		// JMS消息生成器
		jmsConnect.start();
		
	}
	
	private List<String> getDefaultProxyPackagePrefix() {
		List<String> ppp = new ArrayList<String>();
		ppp.add("com.shengpay");
		ppp.add("com.sdp");
		ppp.add("com.sdo");
		ppp.add("com.snda");
		ppp.add("com.shengfutong");
		return ppp;
		
	}
	
	/**
	 * 初始化线程池
	 */
	private ThreadPoolExecutor makeThreadPoolExecutor(int threadPoolSize) {
		return new ThreadPoolExecutor(threadPoolSize, threadPoolSize, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1000), new ThreadPoolExecutor.AbortPolicy());
	}
	
	@Override
	public void onMessage(final Message message) {
		logger.debug("收到一个信息【" + message + "】");
		threadPoolExecutor.execute(new Runnable() {
			@Override
			public void run() {
				processMessage(message);
			}
			
		});
	}
	
	/**
	 * @param message
	 */
	private void processMessage(Message message) {
		//取得被调用方法签名信息
		TextMessage testMessage = (TextMessage) message;
		String methodSign;
		try {
			methodSign = testMessage.getStringProperty(Constants.JMS_PROPERTY_NAME_METHOD_SIGN_FULL);
		} catch (JMSException e) {
			throw new SystemException("JMS服务器端执行服务过程中发生异常", e);
		}
		
		//寻找到被调用方法及其参数信息
		Method method = methodMap.get(methodSign);
		Object[] args;
		try {
			args = JsonUtils.fromJson(method.getParameterTypes(), testMessage.getText());
		} catch (Exception e) {
			throw new SystemException("JMS服务器端执行服务过程中发生异常", e);
		}
		
		//执行被调用方法
		Object returnObj;
		try {
			returnObj = ClassUtils.invokeMethod(consumerObject, method, args);
		} catch (Throwable e) {
			logger.debug("", e);
			returnObj = e.getCause();
		}
		
		// 如果被调用方法的返回值为void,则直接返回,否则将返回信息答复到消息队列里
		if (method.getReturnType() == void.class) {
			return;
		}
		
		// 将方法执行结果回复到消息队列中
		try {
			logger.debug("send return message [" + testMessage.getJMSMessageID() + "]");
			
			String jmsMessageID = testMessage.getJMSMessageID();
			jmsConnect.sendReturnObj(jmsMessageID, returnObj);
		} catch (JMSException e) {
			throw new SystemException("JMS服务器端执行服务过程中发生异常", e);
		}
	}
	
	/**
	 * 将被代理对象进行代理
	 * @param targerObject 
	 * 
	 * @return
	 */
	private Object proxyTargetObject(Object targerObject) {
		ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
		proxyFactoryBean.setInterceptorNames(interceptorNames);
		proxyFactoryBean.setTarget(targerObject);
		proxyFactoryBean.setBeanClassLoader(classLoader);
		proxyFactoryBean.setBeanFactory(beanFactory);
		return proxyFactoryBean.getObject();
	}
	
	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	/**
	 * 针对被调用对象,向jms服务器添加监听
	 * @param targerObject 
	 * 
	 * @param connection
	 * 
	 * @throws JMSException
	 * @throws NamingException
	 */
	private void addListener4Obj(Object targerObject) throws JMSException, NamingException {
		Type[] giArr = targerObject.getClass().getGenericInterfaces();
		for (Type type : giArr) {
			Class<?> interfaceClass = (Class<?>) type;
			if (!interfaceNeedProxy(interfaceClass)) {
				continue;
			}
			listenerInterfaceMethod(interfaceClass);
		}
		
	}
	
	/**
	 * 监听指定接口的所有方法
	 * 
	 * @param interfaceClass
	 * @throws JMSException
	 * @throws NamingException
	 */
	private void listenerInterfaceMethod(Class<?> interfaceClass) throws JMSException, NamingException {
		Method[] methods = interfaceClass.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			putMethodToMap(method);
			
			String filterStr = getMethodFilter(method);
			
			jmsConnect.addConsumer(this, filterStr);
		}
	}
	
	/**
	 * 将方法添加到方法映射中去
	 * 
	 * @param method
	 */
	private void putMethodToMap(Method method) {
		String methodSignFull = ClassUtils.getMethodSignFull(method);
		methodMap.put(methodSignFull, method);
	}
	
	/**
	 * 拼接多个过滤器
	 * 
	 * @param filterArr
	 * @return
	 */
	private String unionFilter(String[] filterArr) {
		StringBuffer buf = new StringBuffer();
		for (String filter : filterArr) {
			if (StringUtils.isBlank(filter)) {
				continue;
			}
			
			if (buf.length() > 0) {
				buf.append(" and ");
			}
			
			buf.append("(").append(filter).append(")");
		}
		
		return buf.toString();
	}
	
	/**
	 * @param classFilterStr
	 * @param classNativeProcessAnn
	 * @return
	 */
	private String appendNpaFilter(String classFilterStr, NativeProcessAnn classNativeProcessAnn) {
		if (classNativeProcessAnn != null) {
			String npaFilterStr = Constants.JMS_PROPERTY_NAME_RESPONSE_AIM_HOST + "='" + getLocalIp() + "'";
			classFilterStr = unionFilter(new String[] { classFilterStr, npaFilterStr });
		}
		return classFilterStr;
	}
	
	/**
	 * @return
	 */
	private String getLocalIp() {
		try {
			return InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			throw new SystemException("获取本机IP信息时发生异常", e);
		}
	}
	
	/**
	 * 取得一个方法的过滤信息
	 * 
	 * @param methodSignFull
	 * @return
	 */
	private String getMethodFilter(Method method) {
		// 方法签名过滤器
		String methodSignFull = ClassUtils.getMethodSignFull(method);
		String methodSignFilter = Constants.JMS_PROPERTY_NAME_METHOD_SIGN_FULL + "='" + methodSignFull + "'";
		
		// 类注解过滤器
		JmsFilterAnn classFilterAnn = targerObject.getClass().getAnnotation(JmsFilterAnn.class);
		String classFilterStr = classFilterAnn != null ? classFilterAnn.filter() : null;
		NativeProcessAnn classNativeProcessAnn = targerObject.getClass().getAnnotation(NativeProcessAnn.class);
		classFilterStr = appendNpaFilter(classFilterStr, classNativeProcessAnn);
		
		// 方法注解过滤器
		Method implMethod = ClassUtils.getMethod(targerObject, method);
		JmsFilterAnn methodFilterAnn = implMethod.getAnnotation(JmsFilterAnn.class);
		NativeProcessAnn methodNativeProcessAnn = implMethod.getAnnotation(NativeProcessAnn.class);
		String methodFilterStr = methodFilterAnn != null ? methodFilterAnn.filter() : null;
		methodFilterStr = appendNpaFilter(methodFilterStr, methodNativeProcessAnn);
		
		// 附加过滤器级别(方法注解>类注解>注入方式)
		String otherFilter = methodFilterStr != null ? methodFilterStr : classFilterStr != null ? classFilterStr : filter;
		
		// 拼接过滤器并返回
		return unionFilter(new String[] { methodSignFilter, otherFilter });
	}
	
	/**
	 * 验证指定接口是否需要JMS监听代理
	 * 
	 * @param interfaceClass
	 * @return
	 */
	private boolean interfaceNeedProxy(Class<?> interfaceClass) {
		String interfaceName = interfaceClass.getName();
		for (String packagePrefix : proxyPackagePrefix) {
			if (interfaceName.startsWith(packagePrefix)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jmsConnect == null) ? 0 : jmsConnect.hashCode());
		result = prime * result + ((targerObject == null) ? 0 : targerObject.hashCode());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MsgListener other = (MsgListener) obj;
		if (jmsConnect == null) {
			if (other.jmsConnect != null)
				return false;
		} else if (!jmsConnect.equals(other.jmsConnect))
			return false;
		if (targerObject == null) {
			if (other.targerObject != null)
				return false;
		} else if (!targerObject.equals(other.targerObject))
			return false;
		return true;
	}
}
