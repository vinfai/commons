package com.shengpay.commons.monitor.mockservice;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.shengpay.commons.monitor.testbase.MonitorTestBase;

/**
 * @title mock service test
 * @description	
 * @usage		
 * @copyright	Copyright 2011 SDO Corporation. All rights reserved.
 * @company		SDO Corporation.
 * @author		wangjinhua <wangjinhua@snda.com>
 * @version		$Id: MockServiceTest.java, v 1.0 Apr 20, 2011 $
 * @create		Apr 20, 2011 4:12:44 PM
 */

public class MockServiceTest extends MonitorTestBase {

	@Autowired
	private MockService mockService;
	
	@Test
	public void testOK1() {
		try {
			mockService.throwBusinessException();
		} catch (Exception e) {
		}
	}
	
	@Test
	public void testOK2() {
		try {
			mockService.throwCheckException();
		} catch (Exception e) {
		}
	}
	@Test
	public void testOK3() {
		try {
			mockService.throwSystemException();
		} catch (Exception e) {
		}
	}
	
}
