/**
 * 
 */
package com.shengpay.commons.core.format;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.shengpay.commons.core.exception.SystemException;

/**
 *  * @description	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDOCorporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: DataFormaterUtil.java,v 1.0 2010-1-31 上午11:24:23 lindc Exp $
 * @create		2010-1-31 上午11:24:23
 */

public class DataFormaterUtil {

	/**
	 * 各种类型的格式化编辑器
	 */
	private static Map<Class<?>,DataFormater<?>> dataFormaterMap=new HashMap<Class<?>,DataFormater<?>>();
	static{
		dataFormaterMap.put(Date.class, new DataFormaterForDate());
		dataFormaterMap.put(BigDecimal.class, new DataFormaterForBigDecimal());
	}
	
	private static DataFormater<?> defaultDataFormater=new DataFormaterForObject();
	
	/**
	 * 根据数据类型信息取得格式化器实例
	 * @param dataClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static DataFormater getInstanceByClass(Class dataClass){
		if(dataClass==null){
			throw new SystemException("参数不合法[getInstanceByClass(Class<T> "+dataClass+")]");
		}

		DataFormater<?> dataFormater = dataFormaterMap.get(dataClass);
		if(dataFormater==null){
			dataFormater=defaultDataFormater;
		}
		return dataFormater;
	}
}
