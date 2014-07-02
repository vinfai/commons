package com.shengpay.commons.core.base;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 判断是否相等时，在属性上添加的注解
 * @author lindongcheng
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface EqualsEnu {

	/**
	 * 计算是否相同时，是否排出该属性
	 * @return
	 */
	boolean except() default false;
}
