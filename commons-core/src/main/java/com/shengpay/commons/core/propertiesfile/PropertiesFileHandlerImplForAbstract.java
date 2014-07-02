package com.shengpay.commons.core.propertiesfile;

import com.shengpay.commons.core.exception.SystemException;



/**
 * 消息资源句柄默认实现
 * @description
 * @author Lincoln
 */
public abstract class PropertiesFileHandlerImplForAbstract implements PropertiesFileHandler {

	/*
	 * (non-Javadoc)
	 */
	public String getString(String key, Object... args) {
		String resource=getStringCanNull(key,args);
		if (resource == null) {
			throw new SystemException("未能找到[" + key + "]对应的消息");
		}

		return resource;
	}

	/* (non-Javadoc)
	 */
	public Boolean getBoolean(String key, Object... args) {
		return Boolean.parseBoolean(getString(key, args));
	}

	/* (non-Javadoc)
	 */
	public Double getDouble(String key, Object... args) {
		return Double.parseDouble(getString(key, args));
	}

	/* (non-Javadoc)
	 */
	public Float getFloat(String key, Object... args) {
		return Float.parseFloat(getString(key, args));
	}

	/* (non-Javadoc)
	 */
	public Integer getInteger(String key, Object... args) {
		return Integer.parseInt(getString(key, args));
	}

	/* (non-Javadoc)
	 */
	public Long getLong(String key, Object... args) {
		return Long.parseLong(getString(key, args));
	}

	/* (non-Javadoc)
	 */
	public String[] getStringList(String key, Object... args) {
		return getString(key, args).split(",");
	}
}
