package com.shengpay.commons.core.utils;

import java.security.*;

public class SHA1Util {
	public static String SHA1(String inStr) {
		MessageDigest md = null;
		String outStr = null;
		try {
			md = MessageDigest.getInstance("SHA-1"); // 选择SHA-1，也可以选择MD5
			final byte[] bytes = inStr.getBytes("UTF-8");
			byte[] digest = md.digest(bytes); // 返回的是byet[]，要转化为String存储比较方便
			outStr = bytetoString(digest);
		} catch (Exception nsae) {
			nsae.printStackTrace();
		}
		return outStr;
	}

	public static String bytetoString(byte[] digest) {
		String str = "";
		String tempStr = "";

		int[] byteArr=new int[digest.length];
		for (int i = 0; i < digest.length; i++) {
			byteArr[i] = (int) (digest[i] & 0xff);
			
		}
		
		for (int i = 1; i < digest.length; i++) {
			tempStr = (Integer.toHexString(byteArr[i]));
			if (tempStr.length() == 1) {
				str = str + "0" + tempStr;
			} else {
				str = str + tempStr;
			}
		}
		return str.toLowerCase();
	}
	
	public static void main(String[] args) {
		System.out.println(SHA1("123456789123456如家快捷12011-11-04 16:46:24 zaq12wsx"));;
	}
}
