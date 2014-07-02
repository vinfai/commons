/**
 * 
 */
package com.shengpay.commons.web.velocity;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.shengpay.commons.core.exception.BusinessException;
import com.shengpay.commons.core.exception.SystemException;



/**
 * cription	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDOCorporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: VelocityHandlerImpl.java,v 1.0 2010-4-19 下午06:40:02 lindc Exp $
 * @create		2010-4-19 下午06:40:02
 */
public class VelocityHandlerImpl implements VelocityHandler {
	/**
	 * Velocity引擎
	 */
	private final VelocityEngine engine = new VelocityEngine();

	/**
	 * 模板文件根目录
	 */
	private String templetDir;

	/**
	 * 初始化Velocity环境
	 */
	public void init() {
		try {
			// 初始化Velocity环境
			Velocity.init();
			// 初始化Velocity引擎
			Properties properties = new Properties();
			properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, templetDir);
			engine.init(properties);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 通过模板名得到引擎填充后的字符串信息
	 */
	public String getContentByString(String strInfo, boolean isTrim, Object... sourceObject) throws BusinessException {
		VelocityContext velocityContext = getVelocityContextByObject(sourceObject);
		String context = getContentByString(strInfo, velocityContext);
		if (isTrim){
			return context.trim();
		}else{
			return context;
		}
	}

	public String getContentByString(String strInfo, Object... sourceObject) throws BusinessException {
		return getContentByString(strInfo, true, sourceObject);
	}

	@Override
	public String getContentByMap(String strInfo, Map<String, Object> contextMap, boolean isTrim) throws BusinessException {
		VelocityContext velocityContext = getVelocityContextByMap(contextMap);
		String context = getContentByString(strInfo, velocityContext);
		if (isTrim){
			return context.trim();
		}else{
			return context;
		}
	}

	/**
	 * 通过模板名得到引擎填充后的字符串信息
	 */
	public String getContentByTemplet(String templetName, Object... sourceObject) throws BusinessException {
		return getContentByTemplet(templetName,true, sourceObject);
	}

	/**
	 * 通过模板名得到引擎填充后的字符串信息
	 */
	public String getContentByTemplet(String templetName,boolean isTrim, Object... sourceObject) throws BusinessException {
		VelocityContext velocityContext = getVelocityContextByObject(sourceObject);
		String content = getContentByTemplet(templetName, velocityContext);
		if(isTrim){
			return content.trim();
		}else{
			return content;
		}
	}

	/**
	 * 返回引擎填充后的字符串信息
	 * 
	 * @param template
	 * @param context
	 * @return
	 */
	String getContentByString(String strInfo, VelocityContext context) {
		StringWriter out = new StringWriter();
		try {
			engine.evaluate(context, out, "velocity", strInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out.toString();
	}

	/**
	 * 通过模板文件返回引擎填充后的文件信息
	 * 
	 * @param template
	 * @param context
	 * @return
	 */
	String getContentByTemplet(String templateFile, VelocityContext context) {
		try {
			Template template = engine.getTemplate(templateFile, "GBK");
			return getContent(template, context);
		} catch (ResourceNotFoundException rnfe) {
			throw new SystemException("未找到模板[" + templateFile + "]", rnfe);
		} catch (ParseErrorException pee) {
			throw new SystemException("无法解析模板[" + templateFile + "]", pee);
		} catch (Exception e) {
			throw new SystemException("获取模板内容时发生异常:", e);
		}

	}

	/**
	 * 得到经引擎转化后的内容,并自动组装Email数组
	 * @param template
	 * @param context
	 * 
	 * @return
	 */
	public String[] getContentByStringAndSplit(String strInfo, String splitChar, Object... sourceObject) throws BusinessException {
		String contentByString = getContentByString(strInfo, sourceObject);
		if (contentByString == null) {
			return null;
		}
		return contentByString.split(splitChar);
	}

	/**
	 * 得到经引擎转化后的内容
	 * 
	 * @param template
	 * @param context
	 * @return
	 */

	private String getContent(Template template, VelocityContext context) {
		try {
			StringWriter writer = new StringWriter();
			template.merge(context, writer);
			return writer.toString();
		} catch (ResourceNotFoundException rnfe) {
			throw new SystemException("未找到模板[" + template + "]", rnfe);
		} catch (ParseErrorException pee) {
			throw new SystemException("无法解析模板[" + template + "]", pee);
		} catch (Exception e) {
			throw new SystemException("获取模板内容时发生异常:", e);
		}

	}

	/**
	 * 根据Map获取Velocity上下文环境
	 * 
	 * @param map
	 * @return
	 */
	private VelocityContext getVelocityContextByMap(Map<String, Object> map) {
		VelocityContext context = new VelocityContext();
		if (map != null) {
			for (Entry<String, Object> entry : map.entrySet()) {
				context.put(entry.getKey(), entry.getValue());
			}
		}
		return context;
	}

	/**
	 * 根据Map获取Velocity上下文环境
	 * 
	 * @param map
	 * @return
	 */
	private VelocityContext getVelocityContextByObject(Object... sourceObject) {
		String prix = "this";
		VelocityContext context = new VelocityContext();
		Object[] args = sourceObject;
		if (sourceObject[0] != null) {
			context.put(prix, args[0]);
		}
		for (int i = 0; i < args.length; i++) {
			String key = prix + String.valueOf(i + 1);
			context.put(key, args[i]);
		}

		return context;
	}

	public String getTempletDir() {
		return templetDir;
	}

	public void setTempletDir(String templetDir) {
		this.templetDir = templetDir;
	}

}
