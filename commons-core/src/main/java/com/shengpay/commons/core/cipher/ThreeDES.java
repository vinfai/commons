package com.shengpay.commons.core.cipher;

import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.utils.CodingUtils;
import com.shengpay.commons.core.utils.StringUtils;

public class ThreeDES {

	private static final String Algorithm = "DESede"; // 定义 加密算法,可用
														// DES,DESede,Blowfish
	private Cipher cipher4Encrypt;
	private Cipher cipher4Decrypt;

	public ThreeDES(String key) {
		if(StringUtils.isBlank(key) || key.length()!=24) {
			throw new SystemException("3des密钥【"+key+"】长度必须为24位！");
		}
		
		// 添加新安全算法,如果用JCE就要把它添加进去
		Security.addProvider(new com.sun.crypto.provider.SunJCE());

		byte[] keybyte = key.getBytes();
		try {
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			cipher4Encrypt = Cipher.getInstance(Algorithm);
			cipher4Encrypt.init(Cipher.ENCRYPT_MODE, deskey);
			cipher4Decrypt = Cipher.getInstance(Algorithm);
			cipher4Decrypt.init(Cipher.DECRYPT_MODE, deskey);
		} catch (Exception e) {
			throw new SystemException("创建3DES实例时发生异常：", e);
		}
	}

	// keybyte为加密密钥，长度为24字节
	// src为被加密的数据缓冲区（源）
	public String encode(String src) {
		try {
			return CodingUtils.bin2hex(cipher4Encrypt.doFinal(src.getBytes()));
		} catch (Exception e) {
			throw new SystemException("加密是发生异常：", e);
		}
	}

	// keybyte为加密密钥，长度为24字节
	// src为加密后的缓冲区
	public String decode(String src) {
		try {
			return new String(cipher4Decrypt.doFinal(CodingUtils.hex2bin(src)));
		} catch (Exception e) {
			throw new SystemException("解密是发生异常：", e);
		}
	}

	public static void main(String[] args) {
		String key="012345678901234567890123";
		ThreeDES td=new ThreeDES(key);
		String src="C1220A41D89B08B30CC4a6D46DC574DC56D428F0EE1E30DFE6875C49C4D15F9D1BA774771C6C3702D20841EE8ABAE4E65";
		String encrypt = td.encode(src);
		System.out.println(encrypt);
		String decrypt = td.decode(encrypt);
		System.out.println(decrypt);
	}

}