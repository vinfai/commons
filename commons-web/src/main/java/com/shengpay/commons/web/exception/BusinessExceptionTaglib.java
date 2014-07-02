package com.shengpay.commons.web.exception;


import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.shengpay.commons.core.exception.BusinessException;
import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.utils.BusinessExceptionUtils;


/**
 * @title 错误信息显示标签
 * @description
 * @author Lincoln
 */
public class BusinessExceptionTaglib extends TagSupport {

	/**
	 * 系统日志输出句柄
	 */
	private final Logger logger = Logger.getLogger(BusinessExceptionTaglib.class);

	/**
	 * 错误信息异常实例在Request中存放的属性名称
	 */
	private String name;

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		//从request中获取错误信息异常,如果未找到,则直接返回
		Object errorInfo = pageContext.getRequest().getAttribute(name);
		logger.debug("获取错误信息异常[name=" + name + "]对象["+errorInfo+"]");
		if (errorInfo == null) {
			logger.debug("未从request中查询到错误信息对象,不再输出错误信息");
			return SKIP_BODY;
		}

		//验证获取到的错误信息异常类型是否合法
		if (!(errorInfo instanceof BusinessException)) {
			throw new SystemException("标签中指定的属性名不为BusinessException类型,请确认后重试[name=" + name + " class=" + errorInfo.getClass().getName() + "]");
		}
 
		//使用错误信息工具箱读取错误信息
		BusinessException businessException = (BusinessException) errorInfo;
		String errorStr = BusinessExceptionUtils.getBusinessInfo(businessException);

		//将错误信息输出
		try {
			pageContext.getOut().println(errorStr);
		} catch (IOException e) {
			throw new SystemException("输出BusinessException信息时发生异常[errorStr=" + errorStr + "]");
		}

		//正常返回
		return SKIP_BODY;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
}
