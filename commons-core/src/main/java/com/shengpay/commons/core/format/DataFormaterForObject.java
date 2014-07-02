/**
 * 
 */
package com.shengpay.commons.core.format;

import com.shengpay.commons.core.exception.SystemException;

/**
 * ription	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDOCorporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: DataFormaterForObject.java,v 1.0 2010-1-31 上午11:50:53 lindc Exp $
 * @create		2010-1-31 上午11:50:53
 */

public class DataFormaterForObject implements DataFormater<Object> {

	@Override
	public Object Parse(String data) {
		throw new SystemException("未知的解析方式[数据:"+data+"]");
	}

	@Override
	public String format(Object data) {
		if(data==null){
			return "";
		}
		return String.valueOf(data);
	}

}
