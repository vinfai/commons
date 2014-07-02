/**
 * 
 */
package com.shengpay.commons.web.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.shengpay.commons.core.exception.BusinessException;
import com.shengpay.commons.web.excel.ExcelView;

/**
 *	spring mvc框架控制器基类 
 * @description	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDOCorporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: BaseController.java,v 1.0 2010-9-20 下午05:33:33 lindongcheng Exp $
 * @create		2010-9-20 下午05:33:33
 */
public abstract class BaseController implements Controller {

	/**
	 * 显示错误信息的页面
	 */
	private String fromPage;
	
	/**
	 * 显示成功信息的页面
	 */
	private String succPage;
	
	/**
	 * 系统错误属性名
	 */
	private String syserrPropertyName="syserr";

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
	 * 在发起页面显示系统性业务信息
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
	public ModelAndView showSyserrBcInFrom(String bcCode, Object... bcParams) {
		return showBcInPage(fromPage, syserrPropertyName, bcCode, bcParams);
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
	 * 在发起页显示系统性业务异常
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
	public ModelAndView showSyserrBeInFrom(BusinessException be) {
		return showObjectInPage(fromPage, syserrPropertyName, be);
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
	public ModelAndView showObjectInPage(String page, String property, Object obj) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put(property, obj);
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

	public String getSyserrPropertyName() {
		return syserrPropertyName;
	}

	public void setSyserrPropertyName(String syserrPropertyName) {
		this.syserrPropertyName = syserrPropertyName;
	}

}
