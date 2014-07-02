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
 * 状态类注解
 * @author lindongcheng
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface StatusAtn {
	Class<?> statusClass();
	
	int value();
	
	String name();
}
