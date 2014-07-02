package com.shengpay.commons.core.cipher;

/**
 * 
 */
public interface CipherHandler {

	/**
	 * 对信息进行加密
	 * 
	 * @param msg
	 * @return
	 */
	String encode(String msg);

	/**
	 * 对信息进行解密
	 * 
	 * @param msg
	 * @return
	 */
	String decode(String msg);

}