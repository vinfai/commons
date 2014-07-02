/**
 * 
 */
package com.shengpay.commons.core.format;

/**
 * 数据格式化接口
 * @description	任意类型的对象格式化成字符串,或从字符串解析成对象的接口
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDOCorporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: DataFormater.java,v 1.0 2010-1-31 上午11:20:30 lindc Exp $
 * @create		2010-1-31 上午11:20:30
 */

public interface DataFormater<T> {

	/**
	 * 格式化对象(将对象转换为便于显示的字符串)
	 * @param data
	 * @return
	 */
	String format(T data);

	/**
	 * 解析对象(将字符串解析成元对象)
	 * @param data
	 * @return
	 */
	T Parse(String data);
}
