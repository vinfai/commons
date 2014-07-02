/**
 * 
 */
package com.shengpay.commons.core.integralitycheck;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lindongcheng
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
@Documented
public @interface IntegralityCheckAtn {
	/**
	 * 当属性为空时抛出业务异常的代码
	 * @return
	 */
	String bcCode4nullValue() default "";
	
	
}
