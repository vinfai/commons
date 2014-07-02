package com.shengpay.commons.core.exception;

import com.shengpay.commons.core.utils.BusinessExceptionUtils;

/**
 * 服务异常
 * 
 * @Title: ServiceException.java
 * @Description: 当业务执行过程中,返回给调用方的处理代码 + 根据业务消息定义的Key得到对应的详细消息
 * @author kuguobing<kuguobing@snda.com>
 * @date 2011-2-12 下午02:01:25
 * @version V1.0
 */
public class ServiceException extends BusinessException {
	private static final long serialVersionUID = 1L;

	// WebService 返回的处理代码
	private String resultCode;

	public ServiceException(String resultCode, String bizErrorKey,
			Object... theArgs) {
		super(bizErrorKey, theArgs);
		this.resultCode = resultCode;
	}

	/**
	 * 返回处理结果代码
	 * 
	 * @return
	 */
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	/**
	 * 得到详细的业务异常消息
	 * 
	 * @return
	 */
	public String getBusinessMessage() {
		return BusinessExceptionUtils.getBusinessInfo(this);
	}

}
