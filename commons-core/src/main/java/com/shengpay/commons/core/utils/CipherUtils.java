/**
 * 
 */
package com.shengpay.commons.core.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import org.apache.log4j.Logger;

/**
 * 
 * DES3加密工具类
 * 
 * @description
 * @usage
 * @copyright Copyright 2010 SDO Corporation. All rights reserved.
 * @company SDO Corporation.
 * @author Panzhou <panzhou@snda.com>
 * @version $Id: CipherUtils.java,v 1.0 2011-3-22 上午10:38:32 panzhou Exp $
 * @create 2011-3-22 上午10:38:32
 */
public class CipherUtils {
	/**
	 * 系统日志输出句柄
	 */
	private static Logger logger = Logger.getLogger(CipherUtils.class);

	private static final String DES3 = "DESede";

	public static String sign(String key, String content) {

		try {
			byte[] password = Base64.decode(key);

			SecretKey sk = new javax.crypto.spec.SecretKeySpec(password, DES3);
			Cipher cipher = Cipher.getInstance(DES3);

			cipher.init(Cipher.ENCRYPT_MODE, sk);

			byte[] encryptedBytes = cipher.doFinal(content.getBytes("utf-8"));

			return Base64.encodeBytes(encryptedBytes, Base64.DONT_BREAK_LINES);
		} catch (Exception e) {
			throw new RuntimeException("DES3加密时发送错误", e);
		}
	}

	public static boolean verify(String signature, String key, String content) {
		if (content == null)
			return false;
		try {
			byte[] password = Base64.decode(key);
			byte[] encryped = Base64.decode(signature);

			SecretKey sk = new javax.crypto.spec.SecretKeySpec(password, DES3);
			Cipher cipher = Cipher.getInstance(DES3);
			cipher.init(Cipher.DECRYPT_MODE, sk);

			byte[] decryptedBytes = cipher.doFinal(encryped);

			String decryptedContent = new String(decryptedBytes, "utf-8");

			return content.equals(decryptedContent);
		} catch (Exception e) {
			throw new RuntimeException("DES3加密时发送错误", e);
		}
	}

}
