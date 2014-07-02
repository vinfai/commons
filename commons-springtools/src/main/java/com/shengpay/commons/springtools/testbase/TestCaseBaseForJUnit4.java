package com.shengpay.commons.springtools.testbase;

import org.junit.Assert;
import org.springframework.aop.framework.Advised;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * spring base test context for junit 4
 * @description	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDO Corporation.
 * @author		wangjinhua <wangjinhua@snda.com>
 * @version		$Id: TestCaseBaseForJUnit4.java, v 1.0 Oct 26, 2010 8:30:30 PM wangjinhua Exp $
 * @create		Oct 26, 2010 8:30:30 PM
 */

public abstract class TestCaseBaseForJUnit4 extends AbstractTransactionalJUnit4SpringContextTests{
	
	/**
	 * 从代理对象得到真实对象
	 * @param <T>
	 * @param proxiedInstance
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> T unwrap(T proxiedInstance) {
		if (proxiedInstance instanceof Advised) {
			try {
				return unwrap((T) ((Advised) proxiedInstance).getTargetSource().getTarget());
			} catch (Exception e) {
				Assert.fail("对代理对象进行unwrap发生异常:" + proxiedInstance.getClass());
			}
		}
		return proxiedInstance;
	}
}
