package com.shengpay1.commons.monitor.mock1service;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.shengpay.commons.monitor.testbase.MonitorTestBase;

/**
 * @title Mock1ServiceTest
 * @description	
 * @usage		
 * @copyright	Copyright 2011 SDO Corporation. All rights reserved.
 * @company		SDO Corporation.
 * @author		wangjinhua <wangjinhua@snda.com>
 * @version		$Id: Mock1ServiceTest.java, v 1.0 Apr 21, 2011 $
 * @create		Apr 21, 2011 9:57:52 AM
 */

public class Mock1ServiceTest extends MonitorTestBase {

	private static final Logger logger = Logger.getLogger(Mock1ServiceTest.class);
	
	@Autowired
	private Mock1Service mock1Service;
	
	@Test
	public void testOK() {
		try {
			logger.info("before Mock1Service");
			mock1Service.throwSystemException();
			logger.info("after Mock1Service");
		} catch (Exception e) {
			logger.info("after Mock1Service throw", e);
		}
	}
}
