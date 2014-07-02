/**
 * 
 */
package com.sdo.transbutton.common.proxyfactroy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;

import com.sdo.transbutton.common.exception.SystemException;
import com.sdo.transbutton.common.utils.ClassUtils;
import com.sdo.transbutton.common.utils.JsonUtils;
import com.sdo.transbutton.common.utils.StringUtils;

/**
 * JMS调用客户端代理对象工厂(消息生产者)
 * 
 * @description
 * @usage
 * @copyright Copyright 2010 SDO Corporation. All rights reserved.
 * @company SDO Corporation.
 * @author LinDongCheng <lindongcheng@snda.com>
 * @version $Id: JmsProxyFactory.java,v 1.0 2010-10-19 下午01:41:59 lindongcheng
 *          Exp $
 * @create 2010-10-19 下午01:41:59
 */

public class JmsProxyFactory implements MessageListener, FactoryBean, InvocationHandler, BeanClassLoaderAware, BeanFactoryAware {

	/**
	 * 拦截器列表
	 */
	private String[] interceptorNames;;

	/**
	 * 系统日志输出句柄
	 */
	private Logger logger = Logger.getLogger(JmsProxyFactory.class);

	/**
	 * JMS属性名:完整方法签名
	 */
	private static final String JMS_PROPERTY_NAME_METHOD_SIGN_FULL = "JMS_PROPERTY_NAME_METHOD_SIGN_FULL";

	/**
	 * jms连接器名称
	 */
	private String jmsConnectName;

	/**
	 * 消息队列名称
	 */
	private String jmsQueueName;

	/**
	 * 被代理的对象
	 */
	private Object targerObject;

	/**
	 * 目标代理接口
	 */
	private Class<?> targetInterface;

	/**
	 * jms会话
	 */
	private Session jmsSession;

	/**
	 * jms消息生产者
	 */
	private List<MessageConsumer> consumerList = new ArrayList<MessageConsumer>();

	/**
	 * jms消息生产者
	 */
	private MessageProducer producer;

	/**
	 * JMS链接器
	 */
	private Connection connection;

	/**
	 * 方法签名到方法信息的映射
	 */
	private Map<String, Method> methodMap = new HashMap<String, Method>();

	/**
	 * 过滤信息
	 */
	private String filter;

	/**
	 * 
	 */
	private ConnectionFactory connectionFactory;

	/**
	 * 
	 */
	private Destination destination;

	/**
	 * 允许被代理的包名前缀
	 */
	private List<String> proxyPackagePrefix;

	/**
	 * spring的容器(用于创建代理工厂)
	 */
	private BeanFactory beanFactory;

	/**
	 * spring容器使用的类加载器(用于创建代理工厂)
	 */
	private ClassLoader classLoader;

	/**
	 * 执行消息消费的对象(经过了若干层的代理)
	 */
	private Object consumerObject;

	/**
	 * 交互返回消息的session
	 */
	private Session receiveReturnSession;

	/**
	 * 初始化JMS上下文环境
	 */
	public void init() {
		
		// 检查被代理接口说对象
		if (targerObject == null && targetInterface == null) {
			throw new SystemException("代理对象和代理接口不能均为空!");
		}

		try {

			connection = connectionFactory.createConnection();

			// JMS会话
			jmsSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			receiveReturnSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// JMS消息生成器
			producer = jmsSession.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);// 设置持久方式

			// 如果存在被代理对象,则启动监听服务
			if (targerObject != null) {
				startListener(connection);
				consumerObject = proxyTargetObject();
			}

		} catch (Throwable e) {
			throw new SystemException("创建JMS连接器过程中发生异常", e);
		}

		logger.info("成功对目标【" + getProxyTarget() + "】进行JMS代理");
	}

	/**
	 * 将被代理对象进行代理
	 * 
	 * @return
	 */
	private Object proxyTargetObject() {
		ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
		proxyFactoryBean.setInterceptorNames(interceptorNames);
		proxyFactoryBean.setTarget(targerObject);
		proxyFactoryBean.setBeanClassLoader(classLoader);
		proxyFactoryBean.setBeanFactory(beanFactory);
		return proxyFactoryBean.getObject();
	}

	/**
	 * 取得代理目标
	 * 
	 * @return
	 */
	private Object getProxyTarget() {
		return targerObject != null ? targerObject : targetInterface;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	/**
	 * 销毁资源
	 */
	public void drop() {
		logger.info("关闭对目标【" + getProxyTarget() + "】的JMS代理");

		// 关闭消息消费者
		for (MessageConsumer concumer : consumerList) {
			try {
				if (consumerList != null) {
					concumer.close();
				}
			} catch (Exception e) {
				logger.error("关闭JMS【" + consumerList + "】时发生异常", e);
			}
		}

		// 关闭消息生产者
		try {
			if (producer != null) {
				producer.close();
			}
		} catch (Exception e) {
			logger.error("关闭JMS【" + producer + "】时发生异常", e);
		}

		// 关闭会话
		try {
			if (jmsSession != null) {
				jmsSession.close();
			}
		} catch (Exception e) {
			logger.error("关闭JMS【" + jmsSession + "】时发生异常", e);
		}

		// 关闭会话
		try {
			if (receiveReturnSession != null) {
				receiveReturnSession.close();
			}
		} catch (Exception e) {
			logger.error("关闭JMS【" + receiveReturnSession + "】时发生异常", e);
		}

		// 关闭连接
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			logger.error("关闭JMS【" + connection + "】时发生异常", e);
		}
	}

	/**
	 * 启动服务器端监听
	 * 
	 * @param connection
	 * 
	 * @throws JMSException
	 * @throws NamingException
	 */
	private void startListener(Connection connection) throws JMSException, NamingException {
		Type[] giArr = targerObject.getClass().getGenericInterfaces();
		for (Type type : giArr) {
			Class<?> interfaceClass = (Class<?>) type;
			if (!interfaceNeedProxy(interfaceClass)) {
				continue;
			}
			listenerInterfaceMethod(interfaceClass);
		}

		// JMS消息生成器
		connection.start();
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

			MessageConsumer consumer = jmsSession.createConsumer(destination, filterStr);
			consumer.setMessageListener(this);
			consumerList.add(consumer);
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
	 * 取得一个方法的过滤信息
	 * 
	 * @param methodSignFull
	 * @return
	 */
	private String getMethodFilter(Method method) {
		// 方法签名过滤器
		String methodSignFull = ClassUtils.getMethodSignFull(method);
		String methodSignFilter = JMS_PROPERTY_NAME_METHOD_SIGN_FULL + "='" + methodSignFull + "'";

		// 类注解过滤器
		JmsFilterAnn classFilterAnn = targerObject.getClass().getAnnotation(JmsFilterAnn.class);
		String classFilterStr = classFilterAnn != null ? classFilterAnn.filter() : null;

		// 方法注解过滤器
		Method implMethod = ClassUtils.getMethod(targerObject, method);
		JmsFilterAnn methodFilterAnn = implMethod.getAnnotation(JmsFilterAnn.class);
		String methodFilterStr = methodFilterAnn != null ? methodFilterAnn.filter() : null;

		// 附加过滤器级别(方法注解>类注解>注入方式)
		String otherFilter = methodFilterStr != null ? methodFilterStr : classFilterStr != null ? classFilterStr : filter;

		// 拼接过滤器并返回
		return unionFilter(new String[] { methodSignFilter, otherFilter });
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

	@Override
	public void onMessage(Message message) {
		//取得被调用方法签名信息
		TextMessage testMessage = (TextMessage) message;
		String methodSign;
		try {
			methodSign = testMessage.getStringProperty(JMS_PROPERTY_NAME_METHOD_SIGN_FULL);
		} catch (JMSException e) {
			throw new SystemException("JMS服务器端执行服务过程中发生异常", e);
		}
		
		//寻找到被调用方法及其参数信息
		Method method = methodMap.get(methodSign);
		Object[] args;
		try {
			args = JsonUtils.fromJson(testMessage.getText());
		} catch (Exception e) {
			throw new SystemException("JMS服务器端执行服务过程中发生异常", e);
		}

		//执行被调用方法
		Object returnObj;
		try {
			returnObj = ClassUtils.invokeMethod(consumerObject, method, args);
		} catch (Throwable e) {
			logger.debug("",e);
			returnObj=e.getCause();
		}

		// 如果被调用方法的返回值为void,则直接返回,否则将返回信息答复到消息队列里
		if (method.getReturnType() == void.class) {
			return;
		}

		// 将方法执行结果回复到消息队列中
		try {
			logger.debug("send return message ["+testMessage.getJMSMessageID()+"]");
			TextMessage returnObjMessage = receiveReturnSession.createTextMessage();
			returnObjMessage.setText(JsonUtils.toJson(new Object[]{returnObj}));
			returnObjMessage.setJMSCorrelationID(testMessage.getJMSMessageID());
			producer.send(returnObjMessage);
		} catch (JMSException e) {
			throw new SystemException("JMS服务器端执行服务过程中发生异常", e);
		}
	}

	@Override
	public Object getObject() throws Exception {
		Class<?>[] proxyInterfaces = targerObject != null ? (Class[]) targerObject.getClass().getGenericInterfaces() : new Class<?>[] { targetInterface };
		return Proxy.newProxyInstance(proxyInterfaces[0].getClassLoader(), proxyInterfaces, this);
	}

	@Override
	public Class<?> getObjectType() {
		return targerObject != null ? targerObject.getClass() : targetInterface;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String methodSignFull = ClassUtils.getMethodSignFull(method);

		// 使用会话创建消息
		TextMessage objMessage =jmsSession.createTextMessage();
		objMessage.setText(JsonUtils.toJson(args));
		objMessage.setStringProperty(JMS_PROPERTY_NAME_METHOD_SIGN_FULL, methodSignFull);// 发布刷新文章消息

		// 取得方法参数信息中可以作为JMS消息属性的映射
		Map<String, Object> propertiesMap = getPropertyMap(method, args, objMessage);

		// 将属性逐个设置到消息体中
		for (Entry<String, Object> entry : propertiesMap.entrySet()) {
			setMessageProperty(objMessage, entry.getKey(), entry.getValue());
		}

		// 使用消息生产者发送消息
		producer.send(objMessage);
		logger.info("成功对方法调用【" + ClassUtils.getMethodCallInfo(method, args) + "】进行了JMS消息发送代理【" + objMessage.getJMSMessageID() + "】");

		// 如果被调用方法无返回值,则直接返回
		if (method.getReturnType() == void.class) {
			return null;
		}

		// 接收并返回方法返回值
		String returnFilter = "JMSCorrelationID='" + objMessage.getJMSMessageID() + "'";
		MessageConsumer consumer = receiveReturnSession.createConsumer(destination, returnFilter);
		logger.debug("receive return message ["+returnFilter+"]");
		TextMessage returnMessage = (TextMessage) consumer.receive();
		
		//如果返回值为异常信息,则抛出,否则返回
		Object[] objArr=JsonUtils.fromJson(returnMessage.getText());
		Object object = objArr[0];
		if(object instanceof Throwable){
			throw (Throwable)object;
		}else{
			return object;
		}
	}

	/**
	 * 取得方法参数信息中可以作为JMS消息属性的映射
	 * 
	 * @param method
	 * @param args
	 * @param objMessage
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws JMSException
	 */
	private Map<String, Object> getPropertyMap(Method method, Object[] args, Message objMessage) throws IllegalArgumentException, IllegalAccessException, JMSException {
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

		// propertiesMap.put(JMS_PROPERTY_NAME_JMS_ID,
		// objMessage.getJMSCorrelationID());
		return propertiesMap;
	}

	/**
	 * 根据消息类型设置消息属性
	 * 
	 * @param testMessage
	 * @param key
	 * @param value
	 * @throws JMSException
	 */
	private void setMessageProperty(Message testMessage, String key, Object value) throws JMSException {
		if(value==null){
			testMessage.setObjectProperty(key, value);
		}else if (value.getClass() == long.class || value.getClass() == Long.class) {
			testMessage.setLongProperty(key, (Long) value);
		} else if (value.getClass() == int.class || value.getClass() == Integer.class) {
			testMessage.setIntProperty(key, (Integer) value);
		} else if (value.getClass() == double.class || value.getClass() == Double.class) {
			testMessage.setDoubleProperty(key, (Double) value);
		} else if (value.getClass() == boolean.class || value.getClass() == Boolean.class) {
			testMessage.setBooleanProperty(key, (Boolean) value);
		} else if (value.getClass() == byte.class || value.getClass() == Byte.class) {
			testMessage.setByteProperty(key, (Byte) value);
		} else if (value.getClass() == float.class || value.getClass() == Float.class) {
			testMessage.setFloatProperty(key, (Float) value);
		} else if (value.getClass() == short.class || value.getClass() == Short.class) {
			testMessage.setShortProperty(key, (Short) value);
		} else if (value.getClass() == String.class) {
			testMessage.setStringProperty(key, (String) value);
		} else {
			testMessage.setObjectProperty(key, String.valueOf(value));
		}
	}

	public String getJmsConnectName() {
		return jmsConnectName;
	}

	public void setJmsConnectName(String jmsConnectName) {
		this.jmsConnectName = jmsConnectName;
	}

	public String getJmsQueueName() {
		return jmsQueueName;
	}

	public void setJmsQueueName(String jmsQueueName) {
		this.jmsQueueName = jmsQueueName;
	}

	public void setTargerObject(Object targerObject) {
		this.targerObject = targerObject;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public void setTargetInterface(Class<?> targetInterface) {
		this.targetInterface = targetInterface;
	}

	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public void setProxyPackagePrefix(List<String> proxyPackagePrefix) {
		this.proxyPackagePrefix = proxyPackagePrefix;
	}

	public void setInterceptorNames(String[] interceptorNames) {
		this.interceptorNames = interceptorNames;
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;

	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
}