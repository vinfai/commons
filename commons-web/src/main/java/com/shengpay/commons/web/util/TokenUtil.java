package com.shengpay.commons.web.util;

import java.math.BigInteger;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
/**
 * 设置同步令牌工具类 		
 * @description	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDOCorporation.
 * @author		lindongchenghelindongchengongcheng@SSDOom>
 * @version		$Id: TokenUtil.java,v 1.2 2009-12-28 下午0lindongchenglindongcheng Exp $
 * @create		2009-12-28 下午04:31:46
 */
public class TokenUtil {
	
	/**
	 * 系统日志输出句柄
	 */
	private static Logger tokenlog = Logger.getLogger(TokenUtil.class);
	
	 /**
     * The default name to map the token value
     */
    public static final String REQUEST_TOKEN_NAME = "requsetToken";
    /**
     * The name of the field which will hold the token name
     */
    public static final String SESSION_NAME_FIELD = "sessionTokenName";
    
    
    private static final Random RANDOM = new Random();
    
    /**
     * 设置默认的同步令牌
     * 
     * @param request
     * @return
     */
    public static String setToken(HttpServletRequest request) {
        return setToken(request,REQUEST_TOKEN_NAME);
    }
    /**
     * 设置同步令牌
     * @param request
     * @param tokenName
     * @return
     */
    public static String setToken(HttpServletRequest request,String tokenName) {
        String token = generateGUID();
        try {
            request.setAttribute(tokenName, token);
        }
        
        catch(IllegalStateException e) {
            // WW-1182 explain to user what the problem is
        	tokenlog.debug("设置session同步令牌错误["+e.getMessage()+"]!");
            throw new IllegalArgumentException(e);
        }
        return token;
    }
    /**
     * 生成随机数
     * 
     * @return
     */
    public static String generateGUID() {
        return new BigInteger(165, RANDOM).toString(36).toUpperCase();
    }
    
    /**
     * 取得sessionToken
     * 
     * @return
     */
    public static String getSessionToken(HttpServletRequest  request) {
        return getSessionToken(request,SESSION_NAME_FIELD);
    }
    
    /**
     * 取得指定标签
     * @param request
     * @param tokenName
     * @return
     */
    public static String getSessionToken(HttpServletRequest request,String tokenName) {
        HttpSession session = request.getSession();
        String token = (String)session.getAttribute(tokenName);

        if ((token == null) || (token.equals(""))) {
        	tokenlog.debug("没有找到指定的令牌号");
            return null;
        }
        return token;
    }
    
   /**
    * 验证同步令牌
    * @param request
    * @return
    */
    public static boolean validToken(HttpServletRequest  request) {
        String requestToken = getReqTokenName(request);

        if (requestToken == null) {
        	tokenlog.debug("no token name found -> Invalid token ");
            return false;
        }
        String sessionToken = getSessionToken(request);
        if (sessionToken == null) {
        	tokenlog.debug("no token found for token name "+requestToken+" -> Invalid token ");
        	request.getSession().setAttribute(SESSION_NAME_FIELD, requestToken);
            return false;
        }
        if (!requestToken.equals(sessionToken)) {
        	tokenlog.debug("同步令牌序不相同");
        	request.getSession().setAttribute(SESSION_NAME_FIELD, requestToken);
            return false;
        }

        return true;
    }
    
    
    public static String getReqTokenName(HttpServletRequest  request) {
        if (request.getParameter(REQUEST_TOKEN_NAME)==null) {
        	tokenlog.debug("Could not find token name in request.");
            return null;
        }
        String tokenName = request.getParameter(REQUEST_TOKEN_NAME);
        return tokenName;
    }
    

}
