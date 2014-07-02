package com.shengpay.commons.core.base;

import java.io.Serializable;

import com.shengpay.commons.core.utils.ClassUtils;

/**
 * 所有自定义类型的基础类
 * 
 * @description 该类可以提供对象信息输出/系统日志输出等公用功能
 * @author Lincoln
 */
public class BaseObject implements Serializable {
	
	/**
	 * 默认当前类版本号
	 */
	private static final long serialVersionUID = 1L;
	
	public static String toString(Object obj) {
		return new ObjectInfoBuilder(obj).getObjectInfo();//ClassUtils.toString(obj);
	}
	
	/**
	 * 输出实例信息
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return toString(this);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		return ClassUtils.equals(this, arg0);
	}
	
	public boolean isNull() {
		return false;
	}
}