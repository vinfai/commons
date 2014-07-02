/**
 * 
 */
package com.shengpay.commons.springtools.proxy.jms;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.shengpay.commons.core.base.BaseObject;
import com.shengpay.commons.core.exception.SystemException;

/**
 * 本地处理JMS消息上下文：用于跟踪消息的属性和过滤信息，以确保应答信息被原来发起请求的服务器取得并处理
 * @copyright	Copyright 2012 SDP Corporation. All rights reserved.
 * @author		lindongcheng <lindongcheng@snda.com>
 * @create		2012-10-24 下午02:38:56
 */

public class NativeProcessContext extends BaseObject{
	
	/**
	 * 当前服务器的HOST信息
	 */
	private String localHost;
	
	/**
	 * @return
	 */
	public NativeProcessContext() {
		try {
			localHost=InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			throw new SystemException("获取本机IP信息时发生异常",e);
		}
	}

	/**
	 * 获取【{@link #localHost localHost}】
	 * @return 类型：String
	 */
	public String getLocalHost() {
		return localHost;
	}
	
}
