/**
 * 
 */
package com.shengpay.commons.core.base;

import com.shengpay.commons.core.exception.BusinessException;


/**
 * 请求处理服务（可以处理以对象形式发起的请求的服务）
 * 
 * @author Administrator
 *
 */
public interface RequestProcessService<T,R> {

	/**
	 * 处理请求
	 * @param request 请求信息
	 * @return 处理结果
	 * @throws BusinessException
	 */
	R processRequest(T request) throws BusinessException;
}
