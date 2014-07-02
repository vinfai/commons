/**
 * 
 */
package com.shengpay.commons.web.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shengpay.commons.core.threadlocal.ThreadLocalUtils;

/**
 * Cookie工具类
 * @description	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDOCorporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: CookieUtils.java,v 1.0 2010-6-5 下午03:52:31 lindc Exp $
 * @create		2010-6-5 下午03:52:31
 */

public class CookieUtils {
	
	/**
	 * 设置支付时使用的银行编号
	 * @param response
	 * @param pbType
	 * @param bankIdStr
	 */
	public static void setPaymentBankId(HttpServletResponse response, String pbType, String bankIdStr) {
		Cookie cookie_bankIdStr = new Cookie(getPaymentBankIdCookieName(pbType), bankIdStr);
		cookie_bankIdStr.setMaxAge(365*24*60*60);
		cookie_bankIdStr.setPath("/");
		response.addCookie(cookie_bankIdStr);
	}

	/**
	 * 获取支付银行编号的cookie名称
	 * @param pbType
	 * @return
	 */
	private static String getPaymentBankIdCookieName(String pbType) {
		return "cookie_"+pbType+"_bankId_"+ThreadLocalUtils.getUserId();
	}

	/**
	 *取得支付时使用的银行编号
	 * @param request
	 * @param pbType
	 * @return
	 */
	public static String getPaymentBankId(HttpServletRequest request, String pbType) {
		String bankIdNameInCookie = getPaymentBankIdCookieName(pbType);
		for(int i=0; request.getCookies() != null && i < request.getCookies().length; i++){
			if(request.getCookies()[i].getName().equals(bankIdNameInCookie)){
				return request.getCookies()[i].getValue().toString();
			}
	    }
		return null;
	}

}
