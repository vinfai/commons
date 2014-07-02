/**
 * 
 */
package com.shengpay.commons.web.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.shengpay.commons.core.base.PaginationBaseObject;
import com.shengpay.commons.core.utils.NumberUtils;

/**
 *  @description 
 * @usage  
 * @copyright Copyright 2010 SDO Corporation. All rights reserved.
 * @company  SDOCorporation.
 * @author  lindongchengonlindongchengng <lindongcheng@SSDOom>
 * @version  $Id: PaginationUtils.java,v 1.2 2010lindongcheng 下午07:13:05 lindongcheng Exp $
 * @create  2010-2-3 下午07:13:05
 */
public class PaginationUtils {
	
	/**
	 * Request Attribute Name : 每页行数
	 */
	public static final String RAN_PAGE_SIZE="RAN_PAGE_SIZE";
	
	/**
	 * Request Attribute Name : 当前页面
	 */
	public static final String RAN_PAGE_NO="RAN_PAGE_NO";

	/**
	 * Request Attribute Name : 当前页信息
	 */
	public static final String RAN_PAGINATION_OBJ="RAN_PAGINATION_OBJ";
	
	/**
	 * @param request
	 * @return
	 */
	public static int getPageSize(HttpServletRequest request){
		return getPageSize(request,10);
	}
	
	/**
	 * 得到一页显示记录数
	 * @param request
	 * @param defaultSize
	 * @return
	 */
	public static int getPageSize(HttpServletRequest request, int defaultSize){
		String pageSize = request.getParameter(RAN_PAGE_SIZE);
		if(NumberUtils.isInteger(pageSize)){
			return Integer.parseInt(pageSize);
		}else{
			return defaultSize;
		}
	}
	
	/**
	 * 得到当前页号
	 * @param request
	 * @return
	 */
	public static int getPageNO(HttpServletRequest request){
		String pageNO = request.getParameter(RAN_PAGE_NO);
		if(NumberUtils.isInteger(pageNO)){
			return Integer.parseInt(pageNO);
		}else{
			return 1;
		}
	}
	
	/**
	 * @param mode
	 * @param pagination
	 */
	@SuppressWarnings("unchecked")
	public static void setPagination(Map mode,PaginationBaseObject pagination){
		mode.put(RAN_PAGINATION_OBJ, pagination);
	}
	
	/**
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static PaginationBaseObject getPagination(HttpServletRequest request){
		return (PaginationBaseObject) request.getAttribute(RAN_PAGINATION_OBJ);
	}
}
