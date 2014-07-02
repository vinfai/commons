/**
 * 
 */
package com.shengpay.commons.core.exception;

/**
 * 人工干预异常
 * @description 系统发生了意料之外的业务异常,且该异常终端用户无法自行处理,需要系统业务人员干预的异常,该种异常将被写入数据库进行待处理
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDO Corporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: ManualMeddleException.java,v 1.0 2010-10-21 下午02:36:35 lindongcheng Exp $
 * @create		2010-10-21 下午02:36:35
 */

public class ManualMeddleException extends BusinessException {

	public ManualMeddleException(String errorCode, Object... theArgs) {
		super(errorCode, theArgs);
	}

}
