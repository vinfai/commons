/**
 * 
 */
package com.shengpay.commons.springtools.proxy.facade;

import com.shengpay.commons.springtools.testbase.CommonTestBase;


/**
 * 
 * @description	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDO Corporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: JmsClientProxyTest.java,v 1.0 2010-10-25 上午11:25:09 lindongcheng Exp $
 * @create		2010-10-25 上午11:25:09
 */

public class FacadeProxyFactoryTest extends CommonTestBase{
	
	/**
	 * 
	 */
	private FacadeInterface facadeInterface;

	/* (non-Javadoc)
	 * @see com.sdo.transbutton.common.proxyfactroy.JmsClientTestService#test(java.lang.String, java.lang.Long)
	 */
	public void test() {
		String name="lindongcheng";
		System.out.println(facadeInterface.sayHello1(name));
		System.out.println(facadeInterface.sayHello2(name));
	}

	public void setFacadeInterface(FacadeInterface facadeInterface) {
		this.facadeInterface = facadeInterface;
	}

}
