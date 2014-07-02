package com.shengpay.commons.core.cer;

import com.shengpay.commons.core.exception.BusinessException;


/**
 * 数字证书处理器
 * @description
 * @author Lincoln
 */
public interface CerHandler {

	/**
	 * 创建数字签名
	 * 
	 * @param keyAlias 私钥别名
	 * @param keyPassword 私钥密码
	 * @param msg 原始信息
	 * @return
	 * @throws BusinessException
	 */
	String createCerSign(String keyAlias, String keyPassword, String msg) throws BusinessException;

	/**
	 * 验证数字签名
	 * 
	 * @param keyAlias 公钥别名
	 * @param msg 原始信息
	 * @param signInfo 被验证的签名信息
	 * @return 验证结果
	 * @throws BusinessException 业务异常
	 */
	boolean validateCerSign(String keyAlias, String msg, String signInfo) throws BusinessException;

}