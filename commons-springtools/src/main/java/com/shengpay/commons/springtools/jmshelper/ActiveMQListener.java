package com.shengpay.commons.springtools.jmshelper;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.log4j.Logger;

/**
 * @title ActiveMQ监听器
 * @description
 * @usage
 * @copyright Copyright 2011 shengpay Corporation. All rights reserved.
 * @company shengpay Corporation.
 * @author LinDongCheng <lindongcheng@snda.com>
 * @version $Id: ActiveMQListener.java,v 1.0 2011-6-8 上午10:42:35 lindongcheng
 *          Exp $
 * @create 2011-6-8 上午10:42:35
 */
public abstract class ActiveMQListener implements MessageListener {
	
	/**
	 * 系统日志输出句柄
	 */
	private Logger log=Logger.getLogger(ActiveMQListener.class);
	
	/**
	 * 链接工厂URI
	 */
	private String connectionFactoryUri;

	/**
	 * 消息队列URI
	 */
	private String queueUri;

	/**
	 * 消息过滤条件
	 */
	private String filterStr;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * jms链接
	 */
	private Connection connect;

	/**
	 * jms会话
	 */
	private Session session;

	/**
	 * @param connectionFactoryUri
	 * @param queueUri
	 * @param userName
	 * @param password
	 */
	public ActiveMQListener(String connectionFactoryUri, String queueUri, String userName, String password) {
		super();
		this.connectionFactoryUri = connectionFactoryUri;
		this.queueUri = queueUri;
		this.userName = userName;
		this.password = password;
	}

	/**
	 * 创建消息监听器
	 * 
	 * @param connectionFactoryUri
	 *            链接工厂uri
	 * @param queueUri
	 *            消息队列uri
	 */
	public ActiveMQListener(String connectionFactoryUri, String queueUri) {
		this.connectionFactoryUri = connectionFactoryUri;
		this.queueUri = queueUri;
	}

	/**
	 * 创建消息监听器
	 * 
	 * @param connectionFactoryUri
	 *            链接工厂uri
	 * @param queueUri
	 *            消息队列uri
	 * @param filterStr
	 *            消息过滤条件
	 */
	public ActiveMQListener(String connectionFactoryUri, String queueUri, String filterStr) {
		super();
		this.connectionFactoryUri = connectionFactoryUri;
		this.queueUri = queueUri;
		this.filterStr = filterStr;
	}

	/**
	 * 启动JMS消息监听
	 */
	public void startJmsListener() {
		try {
			ConnectionFactory factory = getConnectionFactory();
			connect = factory.createConnection();
			session = connect.createSession(false, Session.AUTO_ACKNOWLEDGE);

			Destination destination = getQueue();

			MessageConsumer consumer = session.createConsumer(destination, getFilterStr());
			consumer.setMessageListener(this);

			connect.start();
		} catch (JMSException e) {
			throw new RuntimeException("启动JMS监听器时发生异常：", e);
		}
	}
	
	/**
	 * 停止消息监听并释放各种链接资源
	 */
	public void stopJmsListener(){
		if(session!=null){
			try {
				session.close();
			} catch (JMSException e) {
				log.error("关闭jms监听器会话时发生异常（该异常被屏蔽）：",e);
			}
		}
		
		if(connect!=null){
			try {
				connect.stop();
				connect.close();
			} catch (JMSException e) {
				log.error("关闭jms监听器链接时发生异常（该异常被屏蔽）：",e);
			}
		}
	}

	
	
	/**
	 * 取得过滤条件
	 * 
	 * @return
	 */
	private String getFilterStr() {
		return filterStr;
	}

	/**
	 * 取得被监听的queue实例
	 * 
	 * @return
	 */
	private ActiveMQQueue getQueue() {
		return new ActiveMQQueue(queueUri);
	}

	/**
	 * 取得链接工厂
	 * 
	 * @return
	 */
	private ConnectionFactory getConnectionFactory() {
		return new ActiveMQConnectionFactory(userName, password, connectionFactoryUri);
	}

	public static void main(String[] args) {
		// 参数信息
		String connectionFactoryUri = "failover:(tcp://10.132.4.11:61616)?initialReconnectDelay=100&maxReconnectAttempts=5&jms.useAsyncSend=true";
		String queueUri = "JMS.QUEUE.TRANSBUTTON.lindongcheng_test";

		// 创建并启动监听器
		new ActiveMQListener(connectionFactoryUri, queueUri) {
			public void onMessage(Message msg) {
				try {
					System.out.print("【"+msg.getJMSMessageID() + "】收到消息：");
					if (System.currentTimeMillis() % 2 == 0) {
						System.out.println("发生异常");
						throw new RuntimeException("发生异常！");
					}else{
						msg.acknowledge();
						System.out.println("成功处理");
					}
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.startJmsListener();
		System.out.println("监听器已启动！");

		// 打开网页【http://10.132.4.11:8161/admin/queues.jsp】，发送消息
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		log.info("jms监听器准备关闭...");
		stopJmsListener();
		log.info("jms监听器成功关闭。");
	}

	public Connection getConnect() {
		return connect;
	}

	public Session getSession() {
		return session;
	}
}
