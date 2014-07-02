/**
 * 
 */
package com.shengpay.commons.core.propertiesfile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import com.shengpay.commons.core.exception.SystemException;

/**
 * description
 * @usage
 * @copyright Copyright 2010 SDO Corporation. All rights reserved.
 * @company SDOCorporation.
 * @author LinDongCheng <lindongcheng@snda.com>
 * @version $Id: PropertiesFileHandlerImplForSystemFile.java,v 1.0 2010-1-10
 *          下午07:25:05 lindc Exp $
 * @create 2010-1-10 下午07:25:05
 */

public class PropertiesFileHandlerImplForSystemFile extends PropertiesFileHandlerImplForAbstract {
	/**
	 * 存储资源的属性对象;
	 */
	private Properties properties;

	/**
	 * 构造消息资源句柄
	 * 
	 * @param systemPropertiesFile
	 */
	public PropertiesFileHandlerImplForSystemFile(String systemPropertiesFile) {
		properties=new Properties();
		try {
			properties.load(new FileInputStream(systemPropertiesFile));
		} catch (Exception e) {
			throw new SystemException("无法加载属性文件["+systemPropertiesFile+"]",e);
		}
	}

	/*
	 * (non-Javadoc)
	 */
	public String getStringCanNull(String key, Object... args) {
		String resource=properties.getProperty(key);
		if(resource==null) {
			return null;
		}
		return MessageFormat.format(resource, args);
	}
}
