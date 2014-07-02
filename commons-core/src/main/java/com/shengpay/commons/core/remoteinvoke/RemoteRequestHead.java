package com.shengpay.commons.core.remoteinvoke;

import java.util.Date;

/**
 * @title 		请求的头信息
 * @description	
 * @usage		
 * @copyright	Copyright 2011 shengpay Corporation. All rights reserved.
 * @company		shengpay Corporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: RemoteRequestHeadBaseObject.java,v 1.0 2011-2-21 下午05:17:04 lindongcheng Exp $
 * @create		2011-2-21 下午05:17:04
 */
public class RemoteRequestHead {
	/**
	 * 所属应用编号
	 */
	private String appId;
	/**
	 * 请求编号
	 */
	private String requestId;
	/**
	 * 请求时间
	 */
	private Date requestTime;
	/**
	 * 使用的交互协议版本号
	 */
	private String version;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}