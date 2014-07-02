/**
 * 
 */
package com.shengpay.commons.core.base;

import com.shengpay.commons.core.base.BaseObject;


/**
 * 翻页请求对象
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		2011-9-27 上午11:04:24
 */
public class PaginationRequest extends BaseObject{

	/**
	 * 每页的行数
	 */
	private int pageSize=10;

	/**
	 * 当前页数
	 */
	private int pageNO=1;

	@Deprecated
	public PaginationRequest() {
	}
	
	public PaginationRequest(int pageSize, int pageNO) {
		this.pageSize = pageSize;
		this.pageNO = pageNO;
	}

	/**
	 * 获取【{@link #pageSize pageSize}】（类型：int）
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置【{@link #pageSize pageSize}】（类型：int）
	 */
	@Deprecated
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public void setPageSizeNO(int pageSize,int pageNO) {
		this.pageNO=pageNO;
		this.pageSize=pageSize;
	}

	/**
	 * 获取【{@link #pageNO pageNO}】（类型：int）
	 */
	public int getPageNO() {
		return pageNO;
	}

	/**
	 * 设置【{@link #pageNO pageNO}】（类型：int）
	 */
	@Deprecated
	public void setPageNO(int pageNO) {
		this.pageNO = pageNO;
	}
}
