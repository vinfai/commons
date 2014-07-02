/**
 * 
 */
package com.shengpay.commons.springtools.testbase;

import org.springframework.aop.framework.Advised;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;


/**
 * cription	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDOCorporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: DAOTestBase.java,v 1.0 2010-6-22 下午06:44:16 lindc Exp $
 * @create		2010-6-22 下午06:44:16
 */

public abstract class TestCaseBase extends AbstractTransactionalDataSourceSpringContextTests {
	
	/**
	 * 构造测试对象
	 */
	public TestCaseBase(){
		//不检查依赖验证
		setDependencyCheck(false);
	}
	
	/**
	 * 时差(十分钟)
	 */
	public static final int TIME_DIFF = 1000*60*10;

	@Override
	protected abstract String[] getConfigPaths();
	
	@Override
	public void onTearDown() throws Exception {
		super.onTearDown();
		drop();
	}
	
	protected void drop(){
		
	}

	@Override
	public void onSetUp() throws Exception {
		if (this.applicationContext == null) {
			this.applicationContext = getContext(contextKey());
		}
		prepareTestInstance();
		super.onSetUp();
		init();
	}
	
	/**
	 * 共子类覆写，用于初始化必要的操作
	 */
	protected void init(){
	}
	
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
				fail("对代理对象进行unwrap发生异常:" + proxiedInstance.getClass());
			}
		}
		return proxiedInstance;
	}
}
