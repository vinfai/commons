package com.shengpay.commons.core.utils;

import java.security.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.shengpay.commons.core.valueobject.SupportElementsFactory;

public class SHA256Util {
	public static String SHA(String inStr) {
		MessageDigest md = null;
		String outStr = null;
		try {
			md = MessageDigest.getInstance("SHA-256"); // 选择SHA-1，也可以选择MD5
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

		int[] byteArr = new int[digest.length];
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

	public static void main1(String[] args) {
		List<String> asList = Arrays.asList(new String[] { "merchantNO", "beginTime", "endTime", "merchantTxNO", "terminalNO", "refNO", "orderType" });
		Collections.sort(asList);
		for (String string : asList) {
			System.out.println(string);
		}
		
		System.out.println(SHA("201301010000002013013123595970759212345678912345678900000001737190510031868686868"));
		;
	}
	
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
	}
}
