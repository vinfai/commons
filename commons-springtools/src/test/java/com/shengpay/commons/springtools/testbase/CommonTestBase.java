package com.shengpay.commons.springtools.testbase;


/**
 * DAO基础测试类
 * @description	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDOCorporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: DAOTestBase.java,v 1.0 2010-9-20 下午01:46:42 lindongcheng Exp $
 * @create		2010-9-20 下午01:46:42
 */
public class CommonTestBase extends TestCaseBase {	
	@Override
	protected String[] getConfigPaths() {
		return new String[]{"/META-INF/ac_common_springtools_test_jndi.xml","/META-INF/ac_commons_springtools_service.xml"};
	}
}

