package com.shengpay.commons.web.protocol;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.shengpay.commons.core.exception.BusinessException;
/**
 * 在线支付协议解析服务
 * @description
 * @author Lincoln
 */
public interface ProtocolSignService {

	/**
	 * 加签
	 * @param requestObj 被签名的协议对象
	 * @param keyAlias 私钥在密钥库中的别名
	 * @param keyPassword 私钥在密钥库中的密码
	 * @param requestUrl 打算提交的地址
	 * @param charset TODO
	 * @return 构造的最终URL
	 * @throws BusinessException
	 */
	String sign(Object requestObj, String keyAlias, String keyPassword, String requestUrl, String charset) throws BusinessException;

    /**
     * 加签
     * @param requestObj 被签名的协议对象
     * @param keyAlias 私钥在密钥库中的别名
     * @param keyPassword 私钥在密钥库中的密码
     * @return
     * @throws BusinessException
     */
    public Map<String, String> sign(Object requestObj, String keyAlias, String keyPassword) throws BusinessException;

	/**
	 * 验签:公钥别名通过指定的参数名取得
	 * @param request HTTP请求信息
	 * @param aimClass 解析的目标类型
	 * @param aliasParamName TODO
	 * @return 登记在线支付请求对象
	 * @throws BusinessException 业务异常信息
	 */
	<ProtocolClass> ProtocolClass parseByAliasParamName(HttpServletRequest request, Class<ProtocolClass> aimClass, String aliasParamName) throws BusinessException;

    /**
     * 验签:公钥别名需明确指定
     * @param <ProtocolClass>
     * @param request	HTTP请求信息
     * @param aimClass	协议类型
     * @param cerAlias	用于验签的公钥在密钥库中的别名
     * @return
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public <ProtocolClass> ProtocolClass parseByAliasName(HttpServletRequest request, Class<ProtocolClass> aimClass, String cerAlias) throws BusinessException;

}