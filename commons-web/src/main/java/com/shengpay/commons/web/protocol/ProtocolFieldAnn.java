/**
 * 
 */
package com.shengpay.commons.web.protocol;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日期类型的协议属性注释信息
 * @description	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDOCorporation.
 * @author		lindongchenghelindongchengongcheng@SSDOom>
 * @version		$Id: DateProtocolFieldAnn.java,v 1.2 2010-7-28 下午0lindongchenglindongcheng Exp $
 * @create		2010-7-28 下午05:45:48
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface ProtocolFieldAnn {
	/**
	 * 日期的格式
	 * @return
	 */
	String dateFormat() default "";
	
	/**
	 * 对应的HTTP参数名
	 * @return
	 */
	String paramName() default "";
}
