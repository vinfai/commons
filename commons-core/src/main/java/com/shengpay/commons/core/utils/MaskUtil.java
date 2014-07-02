package com.shengpay.commons.core.utils;


/**
 * 
 * @description	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDO Corporation.
 * @author		Panzhou <panzhou@snda.com>
 * @version		$Id: MaskUtil.java,v 1.0 2010-11-26 下午01:18:45 panzhou Exp $
 * @create		2010-11-26 下午01:18:45
 */
public class MaskUtil {
	public static final String MASK_STRING = "****";

	/**
	 * 得到屏蔽过的email
	 * @param email 不能为空
	 * @return
	 */
	public static String getMaskEmail(String userEmail) {
		int atPos = userEmail.indexOf('@');
		if (atPos < 0) {
			return null;
		} else {
			String viewEmail = userEmail.charAt(0) + MASK_STRING + userEmail.substring(atPos);
			return viewEmail;
		}
	}
	
	/**
	 * 得到屏蔽的手机号
	 * @param userMobile 不能为空
	 * @return
	 */
	public static String getMaskMobile(String userMobile) {
		if (StringUtils.notBlank(userMobile)) {
			return userMobile.substring(0, 3) + MASK_STRING + userMobile.substring(userMobile.length()- 4);
		}
		return null;
	}
	
	/**
	 * 得到屏蔽姓名
	 * @param hoderName 不能为空
	 * @return
	 */
	public static String getMaskName(String hoderName) {
		if (StringUtils.notBlank(hoderName)) {
			return hoderName.substring(0, 1) + "**";
		}
		return null;
	}
	/**
	 * 得到屏蔽信用卡号
	 * @param hoderName 不能为空
	 * @return
	 */
	public static String getMaskCardNumber(String cardNumber) {
		if (StringUtils.notBlank(cardNumber)) {
			return "************" + cardNumber.substring(cardNumber.length() - 4);
		}
		return null;
	}
	
	public static String maskCardNO(String cardNO) {
		if(StringUtils.isBlank(cardNO)) {
			return "";
		}
		
		if(cardNO.length()<10) {
			return "无效卡号";
		}
		
		StringBuffer buf=new StringBuffer(cardNO.substring(0, 6));
		for(int i=0;i<cardNO.length()-10;i++) {
			buf.append("*");
		}
		buf.append(cardNO.substring(cardNO.length()-4));
		return buf.toString();
	}
	
	/**
	 * 判断字段是否已经被屏蔽过
	 * @param str
	 * @return
	 */
	public static boolean isMasked(String str) {
		 if ((str == null) || (str.length() == 0)) {
	            return false;
	        }
	        return str.indexOf('*') >= 0;
	}
	/**
	 * 得到屏蔽通行证账号
	 * @param str 不能为空
	 * @return
	 */
	public static String getMaskUsername(String str) {
		if (StringUtils.notBlank(str)) {
			
			//账号不足3位，全部显示
			if (str.length() <= 3) {
			//	return str.charAt(0) + "**";
				return str;
			}

			int pos = str.indexOf("@");
			//邮箱账号
			if (pos >= 0) {
				return str.charAt(0) + MASK_STRING + str.substring(pos);
			}
			
			//账号含有有后缀，则显示完整的后缀（包括点）
		    pos = str.indexOf('.');
			if (pos >= 0) {
				return str.substring(0, 3) + MASK_STRING + str.substring(pos);
			}
			
			//显示前3位和最后一位字符
			return str.substring(0, 3) + MASK_STRING + str.substring(str.length()-1);
		}
		return null;
	}
	
	public static void main(String args[])
	{
		System.out.println(getMaskEmail("panzhou@snda.com"));
		System.out.println(getMaskMobile("15821900987"));
		System.out.println(getMaskName("panzhou"));
		System.out.println(getMaskUsername("pan"));
	}
}
