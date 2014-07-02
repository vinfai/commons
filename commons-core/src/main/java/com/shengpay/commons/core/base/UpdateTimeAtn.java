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
 * 最后更新时间域注解，在数据入库时将自动取最新时间
 * @author lindongcheng
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface UpdateTimeAtn {
}
