package com.shengpay.commons.monitor.mockservice;

import com.shengpay.commons.core.exception.BusinessException;
import com.shengpay.commons.core.exception.BusinessException.Level;
import com.shengpay.commons.core.exception.CheckException;
import com.shengpay.commons.core.exception.SystemException;

/**
 * @title MockService
 * @description	
 * @usage		
 * @copyright	Copyright 2011 SDO Corporation. All rights reserved.
 * @company		SDO Corporation.
 * @author		wangjinhua <wangjinhua@snda.com>
 * @version		$Id: MockServiceImpl.java, v 1.0 Apr 20, 2011 $
 * @create		Apr 20, 2011 3:58:48 PM
 */

public class MockServiceImpl implements MockService {

	@Override
	public void throwRuntimeException() {
		throw new IllegalArgumentException("runtime exception");
	}

	@Override
	public void throwBusinessException() throws BusinessException {
		throw new BusinessException(Level.INFO, "business exception");
	}

	@Override
	public void throwCheckException() throws CheckException {
		throw new CheckException("check exception");
	}

	@Override
	public void throwSystemException() {
		throw new SystemException("System exception");
	}

}
