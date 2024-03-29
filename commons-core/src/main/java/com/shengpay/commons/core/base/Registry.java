/**
 * 
 */
package com.shengpay.commons.core.base;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @copyright Copyright 2012 SDP Corporation. All rights reserved.
 * @author lindongcheng <lindongcheng@snda.com>
 * @create 2012-11-16 下午05:07:08
 */

public class Registry {

	private static Map<Class<?>, Object> beanMap = new HashMap<Class<?>, Object>();

	private static Map<String, String> propertiesMap = new HashMap<String, String>();

	private static Registry instance = new Registry();

	public static Registry getInstance() {
		return instance;
	}

	public static void setInstance(Registry aInstance) {
		instance = aInstance;
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(Class<T> t) {
		return (T) beanMap.get(t);
	}

	public static <T> T queryBean(Class<T> t) {
		return getInstance().getBean(t);
	}

	public static void addBean(Object obj) {
		if (obj == null) {
			return;
		}

		Class<? extends Object> class1 = obj.getClass();
		while (class1 != null) {
			addBean(class1, obj);
			class1 = class1.getSuperclass();
		}
	}

	public String getProperty(String name) {
		return propertiesMap.get(name);
	}

	public static String queryProperty(String name) {
		return getInstance().getProperty(name);
	}

	public static void addBean(Class<? extends Object> class1, Object obj) {
		beanMap.put(class1, obj);
	}
}
