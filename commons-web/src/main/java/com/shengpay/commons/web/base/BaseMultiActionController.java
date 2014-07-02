package com.shengpay.commons.web.base;

import java.util.HashMap;

import java.util.Map;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.shengpay.commons.core.base.PaginationBaseObject;
import com.shengpay.commons.core.exception.BusinessException;
import com.shengpay.commons.web.util.PaginationUtils;

/**
 * 多方法控制器基础类
 * @description
 * @usage
 * @copyright Copyright 2010 SDO Corporation. All rights reserved.
 * @company SDOCorporation.
 * @author LinDongCheng <lindongcheng@snda.com>
 * @version $Id: BaseMultiActionController.java,v 1.0 2009-11-24 下午12:38:37
 *          lindc Exp $
 * @create 2009-11-24 下午12:38:37
 */
public class BaseMultiActionController extends MultiActionController {

	/**
	 * 显示错误信息的页面
	 */
	private String fromPage;

	/**
	 * 显示成功信息的页面
	 */
	private String succPage;

	/**
	 * 在指定页面显示业务信息
	 * 
	 * @param page
	 *            显示业务信息的页面
	 * @param property
	 *            业务信息在数据模型中的属性名
	 * @param bcCode
	 *            业务编码
	 * @param bcParams
	 *            业务编码参数
	 * @return
	 */
	public ModelAndView showBcInPage(String page, String property, String bcCode, Object... bcParams) {
		return showObjectInPage(page, property, new BusinessException(bcCode, bcParams));
	}

	/**
	 * 在发起页面显示业务信息
	 * 
	 * @param page
	 *            显示业务信息的页面
	 * @param property
	 *            业务信息在数据模型中的属性名
	 * @param bcCode
	 *            业务编码
	 * @param bcParams
	 *            业务编码参数
	 * @return
	 */
	public ModelAndView showBcInFrom(String property, String bcCode, Object... bcParams) {
		return showBcInPage(fromPage, property, bcCode, bcParams);
	}
	
	/**
	 * 在指定页面显示业务异常
	 * 
	 * @param page
	 *            显示业务信息的页面
	 * @param property
	 *            业务信息在数据模型中的属性名
	 * @param bcCode
	 *            业务编码
	 * @param bcParams
	 *            业务编码参数
	 * @return
	 */
	public ModelAndView showBeInPage(String page, String property, BusinessException be) {
		return showObjectInPage(page, property, be);
	}
	
	/**
	 * 在发起页显示业务异常
	 * 
	 * @param page
	 *            显示业务信息的页面
	 * @param property
	 *            业务信息在数据模型中的属性名
	 * @param bcCode
	 *            业务编码
	 * @param bcParams
	 *            业务编码参数
	 * @return
	 */
	public ModelAndView showBeInFrom(String property, BusinessException be) {
		return showObjectInPage(fromPage, property, be);
	}
	
	/**
	 * 在成功页面显示对象信息
	 * 
	 * @param property
	 *            对象在数据模型中的属性名
	 * @param obj
	 *            被显示对象信息
	 * @return
	 */
	public ModelAndView showObjectInSucc(String property, Object obj) {
		return showObjectInPage(succPage, property, obj);
	}

	/**
	 * 在指定页面显示指定对象信息
	 * 
	 * @param property
	 *            对象在数据模型中的属性名
	 * @param obj
	 *            被显示对象信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView showObjectInPage(String page, String property, Object obj) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put(property, obj);
		if(obj instanceof PaginationBaseObject){
			PaginationUtils.setPagination(modelMap, (PaginationBaseObject) obj);
		}
		return new ModelAndView(page, modelMap);
	}
	
	/**
	 * 在指定页面显示指定对象信息（传入已封装页面查询参数的map）
	 * @param modelMap
	 * @param page
	 * @param property
	 * @param obj
	 * @return
	 */
	public ModelAndView showObjectInPage(String page, String property, Object obj,Map<String, Object> modelMap) {
		modelMap.put(property, obj);
		if(obj instanceof PaginationBaseObject){
			PaginationUtils.setPagination(modelMap, (PaginationBaseObject) obj);
		}
		return new ModelAndView(page, modelMap);
	}

	public String getFromPage() {
		return fromPage;
	}

	public void setFromPage(String fromPage) {
		this.fromPage = fromPage;
	}

	public String getSuccPage() {
		return succPage;
	}

	public void setSuccPage(String succPage) {
		this.succPage = succPage;
	}

}
