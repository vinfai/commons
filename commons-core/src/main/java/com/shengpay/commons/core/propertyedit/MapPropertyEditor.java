/**
 * 
 */
package com.shengpay.commons.core.propertyedit;

import java.beans.PropertyEditorSupport;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.utils.CollectionUtils;
import com.shengpay.commons.core.utils.StringUtils;

/**
 * Map属性编辑器
 * @description
 * @usage Lincoln
 */

public class MapPropertyEditor extends PropertyEditorSupport {
	/**
	 * 映射信息前缀:磁盘文件
	 */
	private static String MAP_INFO_PREFIX_FILE = "file:";

	/**
	 * 映射信息前缀:类文件
	 */
	private static String MAP_INFO_PREFIX_CLASSPATH = "classpath:";

	/* (non-Javadoc)
	 * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setAsText(String mapInfo) throws IllegalArgumentException {
		//验证参数是否为空
		mapInfo = mapInfo.trim();
		if (StringUtils.isBlank(mapInfo)) {
			setValue(new LinkedHashMap());
			return;
		}

		//读取MAP信息
		Properties properties = getMapInfoReader(mapInfo);
		
		//转换properties为map
		Map map=CollectionUtils.convertProperties2Map(properties);
		
		//设置对象信息
		setValue(map);
	}

	/**
	 * 取得映射信息阅读器
	 * 
	 * @param mapInfo
	 * @return
	 */
	private Properties getMapInfoReader(String mapInfo) {
		InputStream input = null;
		if (mapInfo.startsWith(MAP_INFO_PREFIX_FILE)) {
			try {
				input = new FileInputStream(mapInfo.substring(MAP_INFO_PREFIX_FILE.length()));
			} catch (FileNotFoundException e) {
				throw new SystemException("读入MAP信息时未找到属性文件[" + mapInfo + "]", e);
			}
		} else if (mapInfo.startsWith(MAP_INFO_PREFIX_CLASSPATH)) {
			input = getClass().getResourceAsStream(mapInfo.substring(MAP_INFO_PREFIX_CLASSPATH.length()));
		} else {
			input = new ByteArrayInputStream(mapInfo.getBytes());
		}

		Properties p=new Properties();
		try {
			p.load(input);
		} catch (IOException e1) {
			throw new IllegalArgumentException("加载属性信息【"+mapInfo+"】时发生异常：",e1);
		}
		return p;
	}

}
