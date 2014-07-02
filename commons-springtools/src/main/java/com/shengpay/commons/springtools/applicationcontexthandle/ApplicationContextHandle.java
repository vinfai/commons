/**
 * 
 */
package com.shengpay.commons.springtools.applicationcontexthandle;

/**
 * 
 * @copyright	Copyright 2012 SDP Corporation. All rights reserved.
 * @author		lindongcheng <lindongcheng@snda.com>
 * @create		2012-11-16 下午04:38:54
 */

public interface ApplicationContextHandle {
	
	/**
	 * 获取Bean
	 * @param <T>
	 * @param t
	 * @return
	 */
	<T> T getBean(Class<T> t);
	
}