package com.shengpay.commons.monitor.mockservice.nonpublic;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.shengpay.commons.core.exception.CheckException;
import com.shengpay.commons.monitor.testbase.MonitorTestBase;

/**
 * @title NonPublicMockServiceImplTest
 * @description	
 * @usage		
 * @copyright	Copyright 2011 SDO Corporation. All rights reserved.
 * @company		SDO Corporation.
 * @author		wangjinhua <wangjinhua@snda.com>
 * @version		$Id: NonPublicMockServiceImplTest.java, v 1.0 Apr 21, 2011 $
 * @create		Apr 21, 2011 10:07:29 AM
 */

public class NonPublicMockServiceImplTest extends MonitorTestBase {

	private static final Logger logger = Logger.getLogger(NonPublicMockServiceImplTest.class);
	
	@Autowired
	private NonPublicMockServiceImpl service;
	
	@Test
	public void testOK1() throws CheckException {
		service.throwProtectedCheckException();
	}
	
	@Test
	public void testOK2() throws CheckException {
		try {
			logger.info("before throwDefaultCheckException");
			service.throwDefaultCheckException();
			logger.info("after throwDefaultCheckException");
		} catch (Exception e) {
			logger.info("after throw throwDefaultCheckException", e);
		}
	}
	
	@Test
	public void testOK3() throws CheckException {
		service.throwPrivateCheckException();
	}
}
