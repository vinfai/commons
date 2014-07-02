/**
 * 
 */
package com.shengpay.commons.core.datacache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 数据缓存注释
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDOCorporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: DataCacheTag.java,v 1.0 2010-2-1 下午07:33:24 lindc Exp $
 * @create		2010-2-1 下午07:33:24
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DataCacheMethodTag {

	/**
	 * 操作类型
	 * @return
	 */
	OperationTypeEnum operationType() default OperationTypeEnum.select;
	
	/**
	 * 缓存策略
	 * @return
	 */
	CacheStrategyEnum cacheStrategy() default CacheStrategyEnum.record;
	
	/**
	 * 关联到的表名称
	 * @return
	 */
	String[] tableName() default {};

}
