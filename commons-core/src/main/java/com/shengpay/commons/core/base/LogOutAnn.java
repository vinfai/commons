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
 * 日志输出注释
 * @description	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDOCorporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: LogOutAnn.java,v 1.0 2010-4-22 下午01:22:43 lindc Exp $
 * @create		2010-4-22 下午01:22:43
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.PARAMETER,ElementType.METHOD})
@Documented
public @interface LogOutAnn {

	/**
	 * 被修饰域是否关闭日志输出功能
	 * @return
	 */
	boolean disableLogOut() default false;
}
