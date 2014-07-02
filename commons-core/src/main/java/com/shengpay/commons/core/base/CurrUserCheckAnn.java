/**
 * 
 */
package com.shengpay.commons.core.base;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ription	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDOCorporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: ThreadLocalCheckAnn.java,v 1.0 2010-2-20 下午03:07:51 lindc Exp $
 * @create		2010-2-20 下午03:07:51
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE })
@Documented
public @interface CurrUserCheckAnn {

	/**
	 * 是否要求用户已登录(默认:已登录)
	 * @return
	 */
	boolean isLogined() default true;
	
	/**
	 * 是否要求用户是激活用户(默认:否)
	 * @return
	 */
	boolean isActive() default false;
	
	/**
	 * 是否要求用户必须为管理员(默认:否)
	 * @return
	 */
	boolean isAdmin() default false;
	
	/**
	 * 是否要求用户必须为普通用户(默认:否)
	 * @return
	 */
	boolean isClient() default false;
}
