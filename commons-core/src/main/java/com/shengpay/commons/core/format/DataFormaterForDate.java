/**
 * 
 */
package com.shengpay.commons.core.format;

import java.util.Date;

import com.shengpay.commons.core.utils.DateTimeUtils;

/**
 * 日期格式化器 			
 * @description	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDOCorporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: DataFormaterForDate.java,v 1.0 2010-1-31 上午11:45:46 lindc Exp $
 * @create		2010-1-31 上午11:45:46
 */

public class DataFormaterForDate implements DataFormater<Date> {

	/* (non-Javadoc)
	 * @see com.mypay.commons.format.DataFormater#Parse(java.lang.String)
	 */
	@Override
	public Date Parse(String data) {
		return DateTimeUtils.parseFullDateTime(data);
	}

	/* (non-Javadoc)
	 * @see com.mypay.commons.format.DataFormater#format(java.lang.Object)
	 */
	@Override
	public String format(Date data) {
		if(data==null){
			return "";
		}
		return DateTimeUtils.formatFullDate(data);
	}

}
