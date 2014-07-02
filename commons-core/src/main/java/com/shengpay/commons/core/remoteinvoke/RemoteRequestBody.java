/**
 * 
 */
package com.shengpay.commons.core.remoteinvoke;

import com.shengpay.commons.core.base.BaseObject;


/**
 * @title 		远程调用请求对象基础类
 * @description	
 * @usage		
 * @copyright	Copyright 2011 shengpay Corporation. All rights reserved.
 * @company		shengpay Corporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: RemoteRequestBaseObject.java,v 1.0 2011-2-21 上午11:12:47 lindongcheng Exp $
 * @create		2011-2-21 上午11:12:47
 */
public class RemoteRequestBody extends BaseObject{
	
	/**
	 * 请求头信息
	 */
	private RemoteRequestHead headInfo;

	/**
	 * 请求的认证信息
	 */
	private AuthObject authInfo;

	public RemoteRequestHead getHeadInfo() {
		return headInfo;
	}

	public void setHeadInfo(RemoteRequestHead headInfo) {
		this.headInfo = headInfo;
	}

	public AuthObject getAuthInfo() {
		return authInfo;
	}

	public void setAuthInfo(AuthObject authInfo) {
		this.authInfo = authInfo;
	}
	
}
