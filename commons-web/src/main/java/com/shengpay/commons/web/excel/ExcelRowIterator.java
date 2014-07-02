/**
 * 
 */
package com.shengpay.commons.web.excel;

import java.util.Iterator;
import java.util.List;

import com.shengpay.commons.core.exception.SystemException;

/**
 * EXCEL行信息迭代器
 * @description	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDOCorporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: ExcelRowIterator.java,v 1.0 2010-1-29 下午07:09:18 lindc Exp $
 * @create		2010-1-29 下午07:09:18
 */

public abstract class ExcelRowIterator<E> implements Iterator<E> {
	/**
	 * 当前数据迭代器
	 */
	private Iterator<E> dataIte;
	
	/**
	 * 当前页码
	 */
	private int pageNum = 1;

	@Override
	public boolean hasNext() {
		if (dataIte == null || !dataIte.hasNext()) {
			dataIte=getDateListIterator(pageNum++).iterator();
		}

		return dataIte.hasNext();
	}

	@Override
	public E next() {
		if (hasNext()) {
			return dataIte.next();
		}
		return null;
	}

	@Override
	public void remove() {
		throw new SystemException("此迭代器不支持该方法!");
	}
	
	/**
	 * 取得数据索引
	 * @return
	 */
	protected abstract List<E> getDateListIterator(int pageNum);

}
