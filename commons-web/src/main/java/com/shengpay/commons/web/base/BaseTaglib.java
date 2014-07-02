package com.shengpay.commons.web.base;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.shengpay.commons.core.exception.BusinessException;
import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.utils.BusinessExceptionUtils;


/**
 * 标签库基础类
 * @description
 * @author Lincoln
 */
public class BaseTaglib extends TagSupport {
	/**
	 * 应用程序上下文环境
	 */
	private WebApplicationContext webApplicationContext;

	/**
	 * 向页面输出信息
	 * 
	 * @param str
	 */
	protected void printInfo(String str) {
		try {
			pageContext.getOut().print(str);
		} catch (IOException e) {
			throw new SystemException(e);
		}
	}

	/**
	 * 输出异常信息
	 * 
	 * @param e
	 */
	protected void printException(Exception e) {
		String exceptionMessage = "[执行标签过程中出现异常:" + e.getMessage() + "]";
		printInfo(exceptionMessage);
	}

	/**
	 * 输出错误信息
	 * 
	 * @param e
	 */
	protected void printException(BusinessException e) {
		printInfo(BusinessExceptionUtils.getBusinessInfo(e));
	}

	/**
	 * 获取HttpServletRequest
	 * 
	 * @return
	 */
	protected HttpServletRequest getRequest() {
		return (HttpServletRequest) pageContext.getRequest();
	}

	/**
	 * 获取HttpSession
	 * 
	 * @return
	 */
	protected HttpSession getSession() {
		return getRequest().getSession();
	}

	/**
	 * 获取ServletContext
	 * 
	 * @return
	 */
	protected ServletContext getServletContext() {
		return getSession().getServletContext();
	}

	/**
	 * 获取ContextPath
	 * 
	 * @return
	 */
	protected String getContextPath() {
		return getRequest().getContextPath();
	}

	/**
	 * 获取web.xml参数信息
	 * 
	 * @return
	 */
	protected String getInitParameter(String paramName) {
		return getServletContext().getInitParameter(paramName);
	}

	/**
	 * 从应用程序上下文环境获取服务句柄
	 * 
	 * @param beanName
	 * @return
	 */
	protected Object getBean(String beanName) {
		if (webApplicationContext == null) {
			webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		}
		return webApplicationContext.getBean(beanName);
	}
}
