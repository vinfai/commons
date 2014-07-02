package com.shengpay.commons.core.constants;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @description
 * @usage
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface ConstantTag {

	/**
	 * 常量域的名称
	 * 
	 * @return
	 */
	String name();

	/**
	 * 常量的类型
	 * 
	 * @return
	 */
	String type();
	
	/**
	 * 反向常量名称（用于转账对方使用）
	 * @return
	 */
	String reverseName() default "";
	
	/**
	 * 所属的父常量：直接提供常量名称
	 * @return
	 */
	String parentConstants() default "";
	
	/**
	 * 转出方转账类型
	 * @return
	 */
	String fromFundTypeFieldName() default "";
	
	/**
	 * 转入方转账类型
	 * @return
	 */
	String toFundTypeFieldName() default "";
	
	/**
	 * 转出方账户类型
	 * @return
	 */
	String fromAbTypeFieldName() default "";
	
	/**
	 * 转入方账户类型
	 * @return
	 */
	String toAbTypeFieldName() default "";
	
	/**
	 * 转出方是否需要查询
	 * @return
	 */
	boolean fromQueryFlag() default true;
	
	/**
	 * 转入方是否需要查询
	 * @return
	 */
	boolean toQueryFlag() default true;
}