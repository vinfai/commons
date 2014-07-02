package com.shengpay.commons.monitor.testbase;

import org.junit.Assert;
import org.springframework.aop.framework.Advised;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * @title 监控测试基类
 * @description	
 * @usage		
 * @copyright	Copyright 2011 SDO Corporation. All rights reserved.
 * @company		SDO Corporation.
 * @author		wangjinhua <wangjinhua@snda.com>
 * @version		$Id: CardAgentServiceTestBase.java, v 1.0 Mar 25, 2011 $
 * @create		Mar 25, 2011 2:42:37 PM
 */

@ContextConfiguration(locations = {  
		"classpath:/META-INF/ac_exception_monitor_jndi_test.xml",
		"classpath:/META-INF/ac_exception_monitor_test.xml"})
public abstract class MonitorTestBase extends AbstractJUnit4SpringContextTests {

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
