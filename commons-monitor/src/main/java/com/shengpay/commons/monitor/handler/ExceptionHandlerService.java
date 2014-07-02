package com.shengpay.commons.monitor.handler;

/**
 * @title ExceptionHandlerService
 * @description	
 * @usage		
 * @copyright	Copyright 2011 SDO Corporation. All rights reserved.
 * @company		SDO Corporation.
 * @author		wangjinhua <wangjinhua@snda.com>
 * @version		$Id: ExceptionHandlerService.java, v 1.0 Apr 21, 2011 $
 * @create		Apr 21, 2011 1:29:43 PM
 */

public interface ExceptionHandlerService {

	void handleException(Throwable e);
	
}
