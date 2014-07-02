package com.shengpay.commons.web.base;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * HTTP SERVLET基础类
 * @description
 * @author Lincoln
 */
public abstract class BaseServletContextListener implements javax.servlet.ServletContextListener {
	/**
	 * 应用程序上下文环境
	 */
	private WebApplicationContext webApplicationContext;
	
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
