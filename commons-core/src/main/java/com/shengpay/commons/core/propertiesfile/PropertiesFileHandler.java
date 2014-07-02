package com.shengpay.commons.core.propertiesfile;


/**
 * 消息资源句柄
 * @description 获取系统资源
 * @author Lincoln
 */
public interface PropertiesFileHandler {

	/**
	 * 取得消息(找不到对将抛出系统异常)
	 * 
	 * @param key 消息键
	 * @param args 消息参数
	 * @return
	 */
	String getString(String key, Object... args);

	/**
	 * 取得消息(找不到对应消息时返回null)
	 * 
	 * @param key 消息键
	 * @param args 消息参数
	 * @return
	 */
	String getStringCanNull(String key, Object... args);

	/**
	 * 取得字符串列表信息(将字符串使用逗号/分号/空格进行分割后返回)
	 * 
	 * @param key 消息键
	 * @param args 消息参数
	 * @return
	 */
	String[] getStringList(String key, Object... args);

	/**
	 * 取得整型信息
	 * 
	 * @param key 消息键
	 * @param args 消息参数
	 * @return
	 */
	Integer getInteger(String key, Object... args);

	/**
	 * 取得长整型信息
	 * 
	 * @param key 消息键
	 * @param args 消息参数
	 * @return
	 */
	Long getLong(String key, Object... args);

	/**
	 * 取得浮点型信息
	 * 
	 * @param key 消息键
	 * @param args 消息参数
	 * @return
	 */
	Float getFloat(String key, Object... args);

	/**
	 * 取得双浮点型信息
	 * 
	 * @param key 消息键
	 * @param args 消息参数
	 * @return
	 */
	Double getDouble(String key, Object... args);

	/**
	 * 取得布尔型信息
	 * 
	 * @param key 消息键
	 * @param args 消息参数
	 * @return
	 */
	Boolean getBoolean(String key, Object... args);

}
