/**
 * 
 */
package com.shengpay.commons.springtools.applicationcontexthandle;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 
 * @copyright	Copyright 2012 SDP Corporation. All rights reserved.
 * @author		lindongcheng <lindongcheng@snda.com>
 * @create		2012-11-16 下午05:07:08
 */

public class ApplicationContextHandleImpl implements ApplicationContextHandle,ApplicationContextAware {
	
	/**
	 * 当前容器
	 */
	private static List<ApplicationContext> acList = new ArrayList<ApplicationContext>();
	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext ac) throws BeansException {
		acList.add(ac);
	}
	
	/* (non-Javadoc)
	 * @see com.shengpay.commons.springtools.base.ApplicationContextHandle#getBean(java.lang.Class)
	 */
	public <T> T getBean(Class<T> t) {
		for (ApplicationContext ac : acList) {
			T bean = ac.getBean(t);
			if (bean != null) {
				return bean;
			}
		}
		
		return null;
	}
	
}
