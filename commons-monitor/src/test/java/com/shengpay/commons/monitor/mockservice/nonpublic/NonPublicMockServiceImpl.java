package com.shengpay.commons.monitor.mockservice.nonpublic;

import org.apache.log4j.Logger;

import com.shengpay.commons.core.exception.CheckException;

/**
 * @title NonPublicMockServiceImpl
 * @description	
 * @usage		
 * @copyright	Copyright 2011 SDO Corporation. All rights reserved.
 * @company		SDO Corporation.
 * @author		wangjinhua <wangjinhua@snda.com>
 * @version		$Id: NonPublicMockServiceImpl.java, v 1.0 Apr 21, 2011 $
 * @create		Apr 21, 2011 10:01:15 AM
 */

public class NonPublicMockServiceImpl {
	
	private static final Logger logger = Logger.getLogger(NonPublicMockServiceImpl.class);

	void throwDefaultCheckException() throws CheckException {
		throw new CheckException("NonPublicMockServiceImpl throwDefaultCheckException");
	}
	
	public void throwProtectedCheckException() throws CheckException {
		try {
			logger.info("before NonPublicMockServiceImpl throwProtectedCheckException0");
			throwProtectedCheckException0();
			logger.info("after NonPublicMockServiceImpl throwProtectedCheckException0");
		} catch (Exception e) {
			logger.info("after throw NonPublicMockServiceImpl throwProtectedCheckException0", e);
		}
	}

	protected void throwProtectedCheckException0() throws CheckException {
		throw new CheckException("NonPublicMockServiceImpl throwProtectedCheckException");
	}
	
	public void throwPrivateCheckException() throws CheckException {
		try {
			logger.info("before NonPublicMockServiceImpl throwPrivateCheckException0");
			throwPrivateCheckException0();
			logger.info("after NonPublicMockServiceImpl throwPrivateCheckException0");
		} catch (Exception e) {
			logger.info("after throw NonPublicMockServiceImpl throwPrivateCheckException0", e);
		}
	}
	
	private void throwPrivateCheckException0() throws CheckException {
		throw new CheckException("NonPublicMockServiceImpl throwPrivateCheckException0");
	}
}
