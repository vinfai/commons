/**
 * 
 */
package com.shengpay.commons.core.constants;

/**
 * @title 		返回码（多用于远程调用的响应信息）
 * @description	
 * @usage		
 * @copyright	Copyright 2011 shengpay Corporation. All rights reserved.
 * @company		shengpay Corporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: RemoteResultCode.java,v 1.0 2011-3-1 下午02:10:27 lindongcheng Exp $
 * @create		2011-3-1 下午02:10:27
 */
public interface ResultCode {

	/**
	 * 返回码：成功
	 */
	String RESULT_SUCC="0000";
	
	/**
	 * 返回码：未知
	 */
	String RESULT_UNKNOW="9999";
}
