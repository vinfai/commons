/**
 * 
 */
package com.shengpay.commons.springtools.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;

import com.shengpay.commons.core.base.Registry;

/**
 * 
 * @copyright Copyright 2012 SDP Corporation. All rights reserved.
 * @author lindongcheng <lindongcheng@snda.com>
 * @create 2012-11-16 下午05:07:08
 */

public class RegistryWithAC extends Registry {

	/**
	 * 当前容器
	 */
	private static List<ApplicationContext> acList = new ArrayList<ApplicationContext>();

	private static Map<String, String> properties = new HashMap<String, String>();

	@Resource
	private ApplicationContext ac;

	@PostConstruct
	public void init() {
		acList.add(ac);
		Registry.setInstance(this);
	}

	public void setProperties(Map<String, String> p) {
		properties.putAll(p);
	}

	public <T> T getBean(Class<T> t) {
		T superBean = super.getBean(t);
		if (superBean != null) {
			return superBean;
		}

		for (ApplicationContext ac : acList) {
			T bean = ac.getBean(t);
			if (bean != null) {
				return bean;
			}
		}

		return null;
	}

	@Override
	public String getProperty(String name) {
		return properties.get(name);
	}

}
