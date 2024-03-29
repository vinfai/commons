package com.shengpay.commons.core.utils;

/**      
 * 二进制编码相关工具方法
* CodingUtils.java Create on 2009-12-21     
*      
* Copyright (c) 2009-12-21 by zhangxiaochuan      
*      
* @author <a href="zhangxc@SDOcom">zhangxiaochuan</a>     
* @version 1.0 
*     
*/
public class CodingUtils {
	/**
	 * 字符串转换成十六进制值
	 * 
	 * @param bin String 我们看到的要转换成十六进制的字符串
	 * @return
	 */
	public static String bin2hex(byte[] bs) {
		char[] digital = "0123456789ABCDEF".toCharArray();
		StringBuffer sb = new StringBuffer("");
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(digital[bit]);
			bit = bs[i] & 0x0f;
			sb.append(digital[bit]);
		}
		return sb.toString();
	}

	/**
	 * 十六进制转换字符串
	 * 
	 * @param hex String 十六进制
	 * @return String 转换后的字符串
	 */
	public static byte[] hex2bin(String hex) {
		String digital = "0123456789ABCDEF";
		char[] hex2char = hex.toCharArray();
		byte[] bytes = new byte[hex.length() / 2];
		int temp;
		for (int i = 0; i < bytes.length; i++) {
			temp = digital.indexOf(hex2char[2 * i]) * 16;
			temp += digital.indexOf(hex2char[2 * i + 1]);
			bytes[i] = (byte) (temp & 0xff);
		}
		return bytes;
	}

	/**
	 * java字节码转字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) { //一个字节的数，   

		// 转成16进制字符串   

		String hs = "";
		String tmp = "";
		for (int n = 0; n < b.length; n++) {
			//整数转成十六进制表示   

			tmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (tmp.length() == 1) {
				hs = hs + "0" + tmp;
			} else {
				hs = hs + tmp;
			}
		}
		tmp = null;
		return hs.toUpperCase(); //转成大写   

	}

	/**
	 * 字符串转java字节码
	 * 
	 * @param b
	 * @return
	 */
	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0) {
			throw new IllegalArgumentException("长度不是偶数");
		}
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			// 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节   

			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		b = null;
		return b2;
	}

	public static void main(String[] args) {
		byte[] content = { -100 };
		String bin2hex = bin2hex(content);
		System.out.println(bin2hex);
		byte[] hex2bin = hex2bin(bin2hex);
		for (int i = 0; i < hex2bin.length; i++) {
			System.out.println(hex2bin[i]);
		}
	}

}
