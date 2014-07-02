package com.shengpay.commons.core.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.shengpay.commons.core.cipher.CipherHandler;
import com.shengpay.commons.core.cipher.CipherHandlerImpl;
import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.propertiesfile.PropertiesFileHandler;
import com.shengpay.commons.core.propertiesfile.PropertiesFileHandlerImplForResourceBundle;

/**
 * 安全相关工具方法
 * @description
 * @author Lincoln
 */
public class SecurityUtils {

	/**
	 * 信息摘要工具[SHA-1]算法
	 */
	static MessageDigest messageDigestBySHA1;
	/**
	 * 信息摘要工具[MD5]算法
	 */
	static MessageDigest messageDigestByMD5;
	static {
		try {
			messageDigestBySHA1 = MessageDigest.getInstance("SHA-1");
			messageDigestByMD5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new SystemException(e);
		}
	}

	private static CipherHandler cipherHandler;
	static {
		/*
		//取得保护业务代码文件的路径信息
		PropertiesFileHandler localConfigHandler = new PropertiesFileHandlerImplForResourceBundle("META-INF/lc_commons");
		String cipherKeyFilePath = localConfigHandler.getString("lc.commons.cipherKeyFilePath");
		String algorithm = localConfigHandler.getString("lc.commons.algorithm");
		cipherHandler = new CipherHandlerImpl(algorithm, cipherKeyFilePath, true);
		*/
	}

	/**
	 * 使用[SHA-1]算法取得指定信息的摘要信息 
	 * 
	 * @param src
	 * @return
	 */
	public static String getMessageDigestBySHA1(String src) {
		if(src==null){
			return null;
		}
		return CodingUtils.bin2hex(messageDigestBySHA1.digest(src.getBytes()));
	}
	/**
	 * 使用[MD5]算法取得指定信息的摘要信息
	 * 
	 * @param src
	 * @return
	 */
	public static String getMessageDigestByMD5(String src) {
		return CodingUtils.bin2hex(messageDigestByMD5.digest(src.getBytes()));
	}
	/**
	 * 验证摘要信息
	 * 
	 * @param str 输入信息(明码）
	 * @param result  结果信息（MD5码）
	 * @return 返回 true 验证通过 false 验证失败
	 */
	public static boolean checkMessagestBySHA1(String str,String result){
		if(StringUtils.isBlank(str))
			return false;
		String newDigestMessage=CodingUtils.bin2hex(messageDigestBySHA1.digest(str.getBytes()));
		if(newDigestMessage.equals(result))
			return true;
		return false;
		
	}
	
	public static String encodePassword(String password){
		return getMessageDigestBySHA1(encode(password));
	}
    
    public static String[] toLowerCase(String[] strArr) {
    	if(strArr==null) {
    		return null;
    	}
    	
    	String[] newStrArr=new String[strArr.length];
    	for(int i=0;i<strArr.length;i++) {
    		String str=strArr[i];
    		newStrArr[i]=str!=null?str.toLowerCase():null;
    	}
    	
    	return newStrArr;
    }
    
	public static String toLowerCase(String str) {
		if(str==null) {
			return null;
		}
		return str.toLowerCase();
	}

	/**
	 * 对信息进行加密
	 * 
	 * @param msg
	 * @return
	 */
	public static String encode(String msg) {
		if(msg==null){
			return null;
		}
		return cipherHandler.encode(msg);
	}

	/**
	 * 对信息进行解密
	 * 
	 * @param msg
	 * @return
	 */
	public static String decode(String msg) {
		return cipherHandler.decode(msg);
	}

	public static void main(String[] args) {
		System.out.println(encodePassword("111111"));
	}
}
