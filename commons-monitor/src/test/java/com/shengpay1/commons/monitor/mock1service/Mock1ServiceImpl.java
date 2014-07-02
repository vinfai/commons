package com.shengpay1.commons.monitor.mock1service;

import com.shengpay.commons.core.exception.SystemException;

/**
 * @title Mock1ServiceImpl
 * @description	
 * @usage		
 * @copyright	Copyright 2011 SDO Corporation. All rights reserved.
 * @company		SDO Corporation.
 * @author		wangjinhua <wangjinhua@snda.com>
 * @version		$Id: Mock1ServiceImpl.java, v 1.0 Apr 21, 2011 $
 * @create		Apr 21, 2011 9:57:11 AM
 */

public class Mock1ServiceImpl implements Mock1Service {

	@Override
	public void throwSystemException() {
		throw new SystemException("mock 1 system exception");
	}

}
