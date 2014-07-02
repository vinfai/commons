package com.shengpay.commons.web.velocity;

import java.util.Map;

import com.shengpay.commons.core.exception.BusinessException;



/**
 * cription	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDOCorporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: VelocityHandler.java,v 1.0 2010-4-19 下午06:39:51 lindc Exp $
 * @create		2010-4-19 下午06:39:51
 */
public interface VelocityHandler {

	/**
	 * 通过摸板文件名和数据对象得到经引擎填充后的文件信息(默认将取得的字符串进行trim)
	 * 
	 * @param templetName
	 * @param sourceObject
	 * @return
	 * @throws BusinessException
	 */
	String getContentByTemplet(String templetName, Object... sourceObject) throws BusinessException;
	
	/**
	 * 通过摸板文件名和数据对象得到经引擎填充后的文件信息(默认将取得的字符串进行trim)
	 * 
	 * @param strinfo
	 * @param sourceObject
	 * @return
	 * @throws BusinessException
	 */
	String getContentByMap(String strinfo, Map<String, Object> contextMap, boolean isTrim) throws BusinessException;

	/**
	 * 通过模板信息和数据对象得到经引擎填充后的字符串信息(默认将取得的字符串进行trim)
	 * 
	 * @param strInfo
	 * @param sourceObject
	 * @return
	 * @throws BusinessException
	 */
	String getContentByString(String strInfo, Object... sourceObject) throws BusinessException;

	/**
	 * 通过模板名得到引擎填充后的字符串信息,并按指定分割符整理成字符串数组
	 * @param strInfo
	 * @param splitChar
	 * @param sourceObject
	 * @return
	 * @throws BusinessException
	 */
	String[] getContentByStringAndSplit(String strInfo, String splitChar, Object... sourceObject) throws BusinessException;

	/**
	 * 通过模板名得到引擎填充后的字符串信息
	 * @param strInfo
	 * @param isTrim
	 * @param sourceObject
	 * @return
	 * @throws BusinessException
	 */
	public String getContentByString(String strInfo, boolean isTrim, Object... sourceObject) throws BusinessException;

	/**
	 * 通过模板名得到引擎填充后的字符串信息
	 * @param templetName
	 * @param isTrim
	 * @param sourceObject
	 * @return
	 * @throws BusinessException
	 */
	public String getContentByTemplet(String templetName, boolean isTrim, Object... sourceObject) throws BusinessException;

}