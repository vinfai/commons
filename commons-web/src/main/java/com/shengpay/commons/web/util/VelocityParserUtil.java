/*
 * File: VelocityParserUtil.java
 * ProjectName: 
 * Description: 
 * 
 * 
 * -----------------------------------------------------------
 * Date            Author          Changes
 * 2010-6-21         wuxin           created
 */
package com.shengpay.commons.web.util;

import java.io.StringWriter;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.ResourceManager;

import com.shengpay.commons.core.utils.ClassUtils;

/**
 * 功能:解析velocity模板
 * <p>
 * 用法:
 * 
 * @version 1.0
 */

public class VelocityParserUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(VelocityParserUtil.class);

	private static VelocityParserUtil instance = new VelocityParserUtil();

	//private VelocityEngine engine = null;

	private VelocityParserUtil() {
		// init engine
		/*engine = new VelocityEngine();
		try {
			engine.init();
		} catch (Exception e) {
			logger.warn("VelocityParserUtil() - exception ignored", e); //$NON-NLS-1$

		}*/
	}

	/**
	 * 返回VelocityParserUtil实例
	 * @return
	 */
	public static VelocityParserUtil getInstance() {
		return instance;
	}

	

	/**
	 * 解析velocity模板
	 * @param vtl
	 * @param model
	 * @return String 
	 * @throws Exception 
	 */
	public String parseVelocityTemplate(String vtl, Map model) throws Exception { 
		if (logger.isDebugEnabled()) {
			logger.debug("parseVelocityTemplate(String, Map) - start"); //$NON-NLS-1$
		}

		VelocityContext velocityContext = new VelocityContext(model);
		StringWriter result = new StringWriter();
		VelocityEngine engine = new VelocityEngine();
		try {
			engine.init();
		} catch (Exception e) {
			logger.info("org.apache.velocity.runtime.resource.ResourceManagerImpl【"+ClassUtils.getUrlByClass(org.apache.velocity.runtime.resource.ResourceManagerImpl.class)+"】                         org.apache.velocity.runtime.resource.ResourceManager:【"+ClassUtils.getUrlByClass(org.apache.velocity.runtime.resource.ResourceManager.class)+"】");
			logger.info("org.apache.velocity.runtime.resource.ResourceManagerImpl【"+org.apache.velocity.runtime.resource.ResourceManagerImpl.class.getClassLoader()+"】                         org.apache.velocity.runtime.resource.ResourceManager:【"+org.apache.velocity.runtime.resource.ResourceManager.class.getClassLoader()+"】");
			logger.info("Thread.currentThread().getContextClassLoader().getClass().getName() = " + Thread.currentThread().getContextClassLoader());
			logger.info("VelocityParserUtil.class.getClassLoader().getClass().getName() = " + ResourceManager.class.getClassLoader());
			logger.warn("parseVelocityTemplate(String, Map) - exception ignored", e);
			throw e; 
		}
		engine.evaluate(velocityContext, result, null, vtl);

		String returnString = result.toString();
		if (logger.isDebugEnabled()) {
			logger.debug("parseVelocityTemplate(String, Map) - end"); //$NON-NLS-1$
		}
		return returnString;

	}

}
