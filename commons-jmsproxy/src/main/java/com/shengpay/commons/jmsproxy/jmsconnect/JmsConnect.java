/**
 * 
 */
package com.shengpay.commons.jmsproxy.jmsconnect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.jndi.JndiObjectFactoryBean;

import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.jmsproxy.utils.JsonUtils;

/**
 * JMS连接
 * @copyright	Copyright 2012 SDP Corporation. All rights reserved.
 * @author		lindongcheng <lindongcheng@snda.com>
 * @create		2012-10-25 下午05:37:46
 */

public class JmsConnect implements BeanClassLoaderAware{
	
	/**
	 * jms消息生产者
	 */
	private List<MessageConsumer> consumerList = new ArrayList<MessageConsumer>();

	/**
	 * 用于发送方法调用请求和接收调用请求信息的JMS会话
	 */
	private Session methodCallJmsSession;
	
	/**
	 * 交互返回消息的session
	 */
	private Session receiveReturnSession;
	
	/**
	 * jms消息生产者
	 */
	private MessageProducer producer;
	
	/**
	 * JMS链接器
	 */
	private Connection connection;
	
	/**
	 * spring容器使用的类加载器(用于创建代理工厂)
	 */
	private ClassLoader classLoader;
	
	/**
	 * 
	 */
	private Destination destination;
	
	/**
	 * 系统日志输出句柄
	 */
	private Logger logger = Logger.getLogger(JmsConnect.class);
	
	/**
	 * @param connectionFactoryJndi
	 * @param destinationJndi
	 */
	public JmsConnect(String connectionFactoryJndi, String destinationJndi) {
		//取得链接工厂和消息队列
		ConnectionFactory connectionFactory = (ConnectionFactory) getObjectByJndi(connectionFactoryJndi);
		destination = (Destination) getObjectByJndi(destinationJndi);
		
		try {
			connection = connectionFactory.createConnection();
			
			// JMS会话
			methodCallJmsSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			receiveReturnSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			// JMS消息生成器
			producer = methodCallJmsSession.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);// 设置持久方式
			
		} catch (Throwable e) {
			drop();
			throw new SystemException("创建JMS连接器过程中发生异常", e);
		}
	}

	private Object getObjectByJndi(String jndiName) {
		JndiObjectFactoryBean jnidFactory = new JndiObjectFactoryBean();
		jnidFactory.setBeanClassLoader(classLoader);
		jnidFactory.setJndiName(jndiName);
		try {
			jnidFactory.afterPropertiesSet();
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException("初始化jndi工厂时发生异常:", e);
		}
		return jnidFactory.getObject();
	}
	

	
	public void sendReturnObj(String jmsMessageID,Object returnObj) throws JMSException {
		TextMessage returnObjMessage = receiveReturnSession.createTextMessage();
		returnObjMessage.setText(JsonUtils.toJson(new Object[] { returnObj }));
		returnObjMessage.setJMSCorrelationID(jmsMessageID);
		producer.send(returnObjMessage);
	}
	
	/**
	 * 销毁资源
	 */
	@PreDestroy
	public void drop() {
		
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
			if (methodCallJmsSession != null) {
				methodCallJmsSession.close();
			}
		} catch (Exception e) {
			logger.error("关闭JMS【" + methodCallJmsSession + "】时发生异常", e);
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
	
	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	/**
	 * @param messageListener 
	 * @param filterStr 
	 * @throws JMSException 
	 * 
	 */
	public void addConsumer(MessageListener messageListener, String filterStr) throws JMSException {
		MessageConsumer consumer = methodCallJmsSession.createConsumer(destination, filterStr);
		consumer.setMessageListener(messageListener);
		consumerList.add(consumer);
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
		if (value == null) {
			testMessage.setObjectProperty(key, value);
		} else if (value.getClass() == long.class || value.getClass() == Long.class) {
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

	/**
	 * @throws JMSException 
	 * 
	 */
	public void start() throws JMSException {
		connection.start();
	}

	/**
	 * @param returnFilter
	 * @return
	 * @throws JMSException 
	 */
	private MessageConsumer createConsumer(String returnFilter) throws JMSException {
		return receiveReturnSession.createConsumer(destination, returnFilter);
	}

	/**
	 * @param jMSMessageID
	 * @param method
	 * @return
	 * @throws JMSException
	 */
	public Object receiveReturnObj(String jMSMessageID, Class<?> returnType) throws JMSException {
		String returnFilter = "JMSCorrelationID='" + jMSMessageID + "'";
		MessageConsumer consumer = createConsumer(returnFilter);
		logger.debug("receive return message [" + returnFilter + "]");
		TextMessage returnMessage = (TextMessage) consumer.receive();
		
		//如果返回值为异常信息,则抛出,否则返回
		Object[] objArr = JsonUtils.fromJson(new Class[] { returnType }, returnMessage.getText());
		Object object = objArr[0];
		return object;
	}

	/**
	 * 发送方法调用信息
	 * @param json
	 * @param propertiesMap
	 * @throws JMSException 
	 */
	public String sendMsg(String json, Map<String, Object> propertiesMap) throws JMSException {
		TextMessage objMessage = methodCallJmsSession.createTextMessage();
		objMessage.setText(json);
		for(Entry<String, Object> e:propertiesMap.entrySet()) {
			setMessageProperty(objMessage,e.getKey(),e.getValue());
		}
		producer.send(objMessage);
		
		return objMessage.getJMSMessageID();
	}
}