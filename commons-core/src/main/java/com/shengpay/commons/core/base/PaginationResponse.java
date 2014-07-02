/**
 * 
 */
package com.shengpay.commons.core.base;

import java.util.ArrayList;
import java.util.List;

import com.shengpay.commons.core.base.BaseObject;

/**
 * 分页基础类
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		2011-9-27 上午11:04:35
 */
public class PaginationResponse<DataType>  extends BaseObject{

	/**
	 * 总行数
	 */
	private int totalRowCount;

	/**
	 * 每页的行数
	 */
	private int pageSize;

	/**
	 * 当前页数
	 */
	private int pageNO;

	/**
	 * 数据列表
	 */
	private List<DataType> dataList = new ArrayList<DataType>();

	/**
	 * 获取【{@link #totalRowCount totalRowCount}】（类型：int）
	 */
	public int getTotalRowCount() {
		return totalRowCount;
	}

	/**
	 * 设置【{@link #totalRowCount totalRowCount}】（类型：int）
	 */
	public void setTotalRowCount(int totalRowCount) {
		this.totalRowCount = totalRowCount;
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
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
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
	public void setPageNO(int pageNO) {
		this.pageNO = pageNO;
	}

	/**
	 * 获取【{@link #dataList dataList}】（类型：List<DataType>）
	 */
	public List<DataType> getDataList() {
		return dataList;
	}

	/**
	 * 设置【{@link #dataList dataList}】（类型：List<DataType>）
	 */
	public void setDataList(List<DataType> dataList) {
		this.dataList = dataList;
	}
}
