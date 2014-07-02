/**
 * 
 */
package com.shengpay.commons.core.datacache;

/**
 * 数据缓存操作类型枚举类型
 * @description	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDOCorporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: DataCacheTypeEnum.java,v 1.0 2010-2-1 下午07:35:05 lindc Exp $
 * @create		2010-2-1 下午07:35:05
 */

public enum OperationTypeEnum {

    insert, delete, update, select, getObjectByFuzzy, findListBySingleFuzzy, findListByMultiFuzzy;
}
