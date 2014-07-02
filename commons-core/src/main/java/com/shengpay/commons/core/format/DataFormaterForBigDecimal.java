/**
 * 
 */
package com.shengpay.commons.core.format;

import java.math.BigDecimal;

/**
 * ription	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDOCorporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: DataFormaterForBigDecimal.java,v 1.0 2010-1-31 上午11:49:03 lindc Exp $
 * @create		2010-1-31 上午11:49:03
 */

public class DataFormaterForBigDecimal implements DataFormater<BigDecimal> {

	@Override
	public BigDecimal Parse(String data) {
		return new BigDecimal(data);
	}

	@Override
	public String format(BigDecimal data) {
		if(data==null){
			return "";
		}
		return data.setScale(2, BigDecimal.ROUND_DOWN).toString();
	}

}
