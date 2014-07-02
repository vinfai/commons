package com.shengpay.commons.monitor.mockservice;

import com.shengpay.commons.core.exception.BusinessException;
import com.shengpay.commons.core.exception.CheckException;

/**
 * @title Mock Service
 * @description	
 * @usage		
 * @copyright	Copyright 2011 SDO Corporation. All rights reserved.
 * @company		SDO Corporation.
 * @author		wangjinhua <wangjinhua@snda.com>
 * @version		$Id: MockService.java, v 1.0 Apr 20, 2011 $
 * @create		Apr 20, 2011 3:58:19 PM
 */

public interface MockService {

	void throwRuntimeException();
	
	void throwSystemException();
	
	void throwCheckException() throws CheckException;
	
	void throwBusinessException() throws BusinessException;
	
	
}
