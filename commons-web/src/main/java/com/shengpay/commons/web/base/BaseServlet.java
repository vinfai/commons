package com.shengpay.commons.web.base;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * HTTP SERVLET基础类
 * @description
 * @author Lincoln
 */
public class BaseServlet extends HttpServlet {
	/**
	 * 应用程序上下文环境
	 */
	private WebApplicationContext webApplicationContext;

	/**
	 * 从应用程序上下文环境获取服务句柄
	 * 
	 * @param beanName
	 * @return
	 */
	protected Object getBean(String beanName) {
		return getBean(beanName,getServletContext());
	}
	
	/**
	 * @param beanName
	 * @param servletContext
	 * @return
	 */
	protected Object getBean(String beanName,ServletContext servletContext) {
		if (webApplicationContext == null) {
			webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		}
		return webApplicationContext.getBean(beanName);
	}
}
