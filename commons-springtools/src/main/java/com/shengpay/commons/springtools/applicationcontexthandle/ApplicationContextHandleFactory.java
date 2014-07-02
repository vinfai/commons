/**
 * 
 */
package com.shengpay.commons.springtools.applicationcontexthandle;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.shengpay.commons.core.threadlocal.ThreadLocalUtils;

/**
 * 
 * @copyright	Copyright 2012 SDP Corporation. All rights reserved.
 * @author		lindongcheng <lindongcheng@snda.com>
 * @create		2012-11-16 下午01:11:41
 */

public class ApplicationContextHandleFactory{

	
	/**
	 * 单例
	 */
	private static ApplicationContextHandle instance;
	
	/**
	 * 获取单例
	 * @return
	 */
	public static ApplicationContextHandle getInstance() {
		if(instance==null) {
			instance=new ApplicationContextHandleImpl();
		}
		return instance;
	}
	

	/**
	 * 设置【{@link #instance instance}】
	 * @param 类型：ApplicationContextHandleImpl
	 */
	public static void setInstance(ApplicationContextHandle instance) {
		ApplicationContextHandleFactory.instance = instance;
	}
	
}
