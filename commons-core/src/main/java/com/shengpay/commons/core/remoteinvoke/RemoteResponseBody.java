/**
 * 
 */
package com.shengpay.commons.core.remoteinvoke;

import com.shengpay.commons.core.base.BaseObject;

/**
 * @title 		远程应答信息基础类
 * @description	
 * @usage		
 * @copyright	Copyright 2011 shengpay Corporation. All rights reserved.
 * @company		shengpay Corporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: RemoteResponseBaseObject.java,v 1.0 2011-2-21 下午01:27:01 lindongcheng Exp $
 * @create		2011-2-21 下午01:27:01
 */
public class RemoteResponseBody extends BaseObject{
	
	/**
	 * 请求头信息
	 */
	private RemoteRequestHead headInfo;
	
	/**
	 * 处理结果
	 */
	private String result;   
	
	/**
	 * 错误信息 
	 */
	private String errorMsg;
	
	/**
	 * 处理编号
	 */
	private String processNO;
	
	/**
	 * 处理时间
	 */
	private String processTime;
	
	/**
	 * 请求的认证信息:应答信息的认证信息，和请求的认证信息数据不同
	 */
	private AuthObject authInfo;
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getProcessNO() {
		return processNO;
	}

	public void setProcessNO(String processNO) {
		this.processNO = processNO;
	}

	public String getProcessTime() {
		return processTime;
	}

	public void setProcessTime(String processTime) {
		this.processTime = processTime;
	}

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
