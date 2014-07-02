/**
 * 
 */
package com.shengpay.commons.springtools.testbase;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 压力测试注释
 * @description	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDOCorporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: LoadTest.java,v 1.0 2010-8-4 下午04:07:24 lindc Exp $
 * @create		2010-8-4 下午04:07:24
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface LoadTestAnn {
	/**
	 * 能够接受的最大运行时间
	 * @return
	 */
	int maxRunMill();
	
	/**
	 * 每个线程运行的次数
	 * @return
	 */
	int repeatNum();

	/**
	 * 使用的线程数
	 * @return
	 */
	int threadNum();
	
}
