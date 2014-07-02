package com.shengpay.commons.core.remoteinvoke;

/**
 * @title 		请求的认证信息
 * @description	
 * @usage		
 * @copyright	Copyright 2011 shengpay Corporation. All rights reserved.
 * @company		shengpay Corporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: RemoteRequestAuthObjectData.java,v 1.0 2011-2-21 下午05:18:00 lindongcheng Exp $
 * @create		2011-2-21 下午05:18:00
 */
public class AuthObject {
	/**
	 * 认证方式
	 */
	private String authType;
	/**
	 * 认证信息
	 */
	private String authInfo;
	
	public String getAuthType() {
		return authType;
	}
	public void setAuthType(String authType) {
		this.authType = authType;
	}
	public String getAuthInfo() {
		return authInfo;
	}
	public void setAuthInfo(String authInfo) {
		this.authInfo = authInfo;
	}
}