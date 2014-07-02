/**
 * 
 */
package com.shengpay.commons.springtools.proxy.jms;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * JMS消息属性注解
 * @description	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDO Corporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: JmsPropertyAnn.java,v 1.0 2010-10-25 下午03:51:28 lindongcheng Exp $
 * @create		2010-10-25 下午03:51:28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER,ElementType.FIELD})
@Documented
public @interface JmsPropertyAnn {

	String property();
}
