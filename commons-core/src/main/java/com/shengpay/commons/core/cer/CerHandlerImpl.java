/**
 * 
 */
package com.shengpay.commons.core.cer;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;

import com.shengpay.commons.core.exception.BusinessException;
import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.utils.CodingUtils;

/**
 * 数字证书处理器
 * @description
 * @author Lincoln
 */

public class CerHandlerImpl implements CerHandler {

	/**
	 * 密钥库
	 */
	private KeyStore keyStore;

	/**
	 * 原始信息使用的字符集
	 */
	private String msgCharsetName;

	/**
	 * 密钥库路径
	 */
	private String keyStorePath;

	/**
	 * 密钥库类型
	 */
	private String keyStoreType;

	/**
	 * 密钥库密码
	 */
	private String keyStorePswd;

	/**
	 * 初始化数字证书处理器
	 */
	public void init() {
		try {
			keyStore = KeyStore.getInstance(keyStoreType);
			keyStore.load(new FileInputStream(keyStorePath), keyStorePswd.toCharArray());
		} catch (Exception e) {
			throw new SystemException("加载密钥库信息时发生异常:", e);
		}
	}

	/* (non-Javadoc)
	 */
	public boolean validateCerSign(String keyAlias, String msg, String signInfo) throws BusinessException {
		//验证参数合法性
		if (keyAlias == null || msg == null || signInfo == null) {
			throw new SystemException("参数不合法[validateCerSign(String " + keyAlias + ", String " + msg + ", String " + signInfo + ")]");
		}

		//取得公钥
		Certificate certificate = null;
		try {
			certificate = keyStore.getCertificate(keyAlias);
		} catch (Exception e) {
			throw new SystemException("从密钥库取得数字证书[" + keyAlias + "]时发生异常:", e);
		}
		PublicKey key = certificate.getPublicKey();

		//取得验证器
		Signature sig = null;
		try {
			sig = Signature.getInstance("SHA1With" + key.getAlgorithm());
		} catch (Exception e) {
			throw new SystemException("初始化验证器时发生异常:", e);
		}

		//初始化原始数据
		try {
			sig.initVerify(key);
			sig.update(msg.getBytes(msgCharsetName));
		} catch (Exception e) {
			throw new SystemException("初始化原始数据时发生异常:", e);
		}

		//执行数字证书验证
		byte[] signByteArr = CodingUtils.hex2bin(signInfo);
		try {
			return sig.verify(signByteArr);
		} catch (Exception e) {
			throw new SystemException("执行数字证书验证时发生异常:", e);
		}
	}

	/* (non-Javadoc)
	 */
	public String createCerSign(String keyAlias, String keyPassword, String msg) throws BusinessException {
		//验证参数合法性
		if (keyAlias == null || keyPassword == null || msg == null) {
			throw new SystemException("参数不合法[validateCerSign(String " + keyAlias + ",String " + keyPassword + ", String " + msg + ")]");
		}

		//取得私钥
		PrivateKey key;
		try {
			key = (PrivateKey) keyStore.getKey(keyAlias, keyPassword.toCharArray());
		} catch (Exception e) {
			throw new SystemException("从密钥库取得密钥信息[" + keyAlias + "]时发生异常:", e);
		}

		//取得验证器
		Signature sig = null;
		try {
			sig = Signature.getInstance("SHA1With" + key.getAlgorithm());
		} catch (Exception e) {
			throw new SystemException("初始化验证器时发生异常:", e);
		}

		//初始化原始数据
		try {
			sig.initSign(key);
			sig.update(msg.getBytes(msgCharsetName));
		} catch (Exception e) {
			throw new SystemException("初始化原始数据时发生异常:", e);
		}

		//执行数字签名并返回结果
		byte[] signByteArr = null;
		try {
			signByteArr = sig.sign();
		} catch (Exception e) {
			throw new SystemException("执行签名时发生异常:", e);
		}
		return CodingUtils.bin2hex(signByteArr);

	}

	/**
	 * @return the msgCharsetName
	 */
	public String getMsgCharsetName() {
		return msgCharsetName;
	}

	/**
	 * @param msgCharsetName the msgCharsetName to set
	 */
	public void setMsgCharsetName(String msgCharsetName) {
		this.msgCharsetName = msgCharsetName;
	}

	/**
	 * @return the keyStorePath
	 */
	public String getKeyStorePath() {
		return keyStorePath;
	}

	/**
	 * @param keyStorePath the keyStorePath to set
	 */
	public void setKeyStorePath(String keyStorePath) {
		this.keyStorePath = keyStorePath;
	}

	/**
	 * @return the keyStoreType
	 */
	public String getKeyStoreType() {
		return keyStoreType;
	}

	/**
	 * @param keyStoreType the keyStoreType to set
	 */
	public void setKeyStoreType(String keyStoreType) {
		this.keyStoreType = keyStoreType;
	}

	/**
	 * @return the keyStorePswd
	 */
	public String getKeyStorePswd() {
		return keyStorePswd;
	}

	/**
	 * @param keyStorePswd the keyStorePswd to set
	 */
	public void setKeyStorePswd(String keyStorePswd) {
		this.keyStorePswd = keyStorePswd;
	}
}
