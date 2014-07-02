/**
 * 
 */
package com.shengpay.commons.core.base;

import java.util.ArrayList;
import java.util.List;

/**
 *  分页基础类
 * @description
 * @author Lincoln
 */

public class PaginationBaseObject<DataType> extends BaseObject {

	/**
	 * 总行数
	 */
	private int totalRowCount;

	/**
	 * 总页数
	 */
	private int totalPageCount;

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
	private List<DataType> dataList = new ArrayList<DataType>();;

	/**
	 * 设置分页信息
	 * 
	 * @param totalRowCount
	 * @param pageSize
	 * @param pageNo
	 */
	public void setPagination(int totalRowCount, int pageSize, int pageNO) {
		this.totalRowCount = totalRowCount;
		this.pageSize = pageSize;
		this.pageNO = pageNO;
		if(totalRowCount==0){
			totalPageCount=1;
		}else{
			totalPageCount = (totalRowCount / pageSize) + (totalRowCount % pageSize > 0 ? 1 : 0);
		}
	}

	/**
	 * 计算指定页在特定页行数下的第一行行号是多少;
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public static int startRowNum(int pageNum, int pageSize) {
		return (pageNum - 1) * pageSize;
	}

	/**
	 * 是否存在下一页
	 * 
	 * @return
	 */
	public boolean hasNextPage() {
		return pageNO < totalPageCount;
	}

	/**
	 * 是否存在上一页
	 * 
	 * @return
	 */
	public boolean hasPrePage() {
		return pageNO > 1;
	}

	/**
	 * 取得下一页页码
	 * 
	 * @return
	 */
	public int getNextPageNO() {
		return hasNextPage() ? pageNO + 1 : pageNO;
	}

	/**
	 * 取得上一页页码
	 * 
	 * @return
	 */
	public int getPrePageNO() {
		return hasPrePage() ? pageNO - 1 : pageNO;
	}

	/**
	 * @return the totalRowCount
	 */
	public int getTotalRowCount() {
		return totalRowCount;
	}

	/**
	 * @return the totalPageCount
	 */
	public int getTotalPageCount() {
		return totalPageCount;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @return the pageNO
	 */
	public int getPageNO() {
		return pageNO;
	}

	/**
	 * @return the dataList
	 */
	public List<DataType> getDataList() {
		return dataList;
	}
	
	public DataType getFirstData() {
		if(dataList==null || dataList.size()==0) {
			return null;
		}
		
		return dataList.get(0);
	}

	/**
	 * @param dataList
	 *            the dataList to set
	 */
	public void setDataList(List<DataType> dataList) {
		this.dataList = dataList;
	}
	
	public void addData(DataType data){
		dataList.add(data);
	}
}
