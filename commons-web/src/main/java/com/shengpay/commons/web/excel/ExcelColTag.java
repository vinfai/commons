package com.shengpay.commons.web.excel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * EXCEL列属性标签
 * @description	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDOCorporation.
 * @author		lindongchenghelindongchengongcheng@SSDOom>
 * @version		$Id: ExcelColTag.java,v 1.2 2010-1-29 下午0lindongchenglindongcheng Exp $
 * @create		2010-1-29 下午05:24:42
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface ExcelColTag {

	/**
	 * 列名称
	 * @return
	 */
	String getTitle();
	
	/**
	 * 列序号
	 * @return
	 */
	int getOrder();
}
