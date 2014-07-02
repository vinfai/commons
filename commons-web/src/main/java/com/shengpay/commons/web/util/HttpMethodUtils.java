package com.shengpay.commons.web.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import com.shengpay.commons.core.exception.CheckException;
import com.shengpay.commons.core.exception.SystemException;

/**
 * Http method 工具方法
 * @description	 
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDO Corporation.
 * @author 		wangjinhua
 * @version		$Id: UserLoginCtrl.java,v 1.2 2009-12-4 下午02:31:10 chenjianxiao Exp $
 * @create		2009-12-4 下午02:31:10
 *
 */
public class HttpMethodUtils {
//    private static final Logger log = Logger.getLogger(HttpMethodUtils.class);
    
	private static final HttpClient HTTP_CLIENT = new HttpClient();

    {
		HTTP_CLIENT.getParams().setCookiePolicy(
				CookiePolicy.BROWSER_COMPATIBILITY);
    }
    
	/**
	 * 模拟浏览器get方法，对url进行请求
	 * @param url		请求url(必填)
	 * @return			请求结果
	 */
	public static String getRequest(String url) {
		String responseBody = null;
		HttpMethod method = new GetMethod(url);
		int httpCode = 0;
		try {
			httpCode = HTTP_CLIENT.executeMethod(method);
			responseBody = new String(method.getResponseBody());
		} catch (HttpException e) {
			throw new SystemException("创建http getRequest请求发生异常【请求url:" + url + ", httpCode:"
					+ httpCode + " 】:", e);
		} catch (IOException e) {
			throw new SystemException("创建http getRequest请求发生异常【请求url:" + url + ", httpCode:"
					+ httpCode + " 】:", e);
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return responseBody;
	}
	
	/**
	 * 模拟浏览器get方法，对url进行请求
	 * @param url		请求url(必填)
	 * @return			请求结果，使用GBK编码
	 */
	public static String getRequestGBK(String url) throws CheckException {
		return getRequest(url, "GBK");
	}

	/**
	 * 模拟浏览器get方法，对url进行请求
	 * @param url		请求url(必填)
	 * @return			请求结果，使用UTF-8编码
	 */
	public static String getRequestUTF8(String url) throws CheckException {
		return getRequest(url, "UTF-8");
	}

	/**
	 * 模拟浏览器get方法，对url进行请求
	 * @param url		请求url(必填)
	 * @return			请求结果，使用charSet编码
	 */
	private static String getRequest(String url, String charSet) throws CheckException {
		try {
			return new String(getRequestWithByteArray(url), charSet);
		} catch (UnsupportedEncodingException e) {
			throw new SystemException("创建http getRequest请求发生异常，对返回byte数组编码发生异常【请求url:" + url
					+ ", charSet:" + charSet + " 】:", e);
		}
	}
    
	/**
	 * 模拟浏览器get方法，对url进行请求
	 * @param url		请求url(必填)
	 * @return	请求结果
	 */
	public static byte[] getRequestWithByteArray(String url) throws CheckException {
		byte[] responseByteArray = null;
		HttpMethod method = new GetMethod(url);
		int httpCode = 0;
		BufferedInputStream in = null;
		ByteArrayOutputStream out = null;
		try {
			httpCode = HTTP_CLIENT.executeMethod(method);
			responseByteArray = method.getResponseBody();
			in = new BufferedInputStream(method.getResponseBodyAsStream());
			out = new ByteArrayOutputStream(4*1024);
			byte[] temp = new byte[4*1024];
			int size = 0;
			while ((size = in.read(temp)) != -1) {
	            out.write(temp, 0, size);
	        }
			responseByteArray = out.toByteArray();
		} catch (HttpException e) {
			throw new CheckException(
					"创建http getRequestWithByteArray请求发生异常【请求url:" + url
							+ ", httpCode:" + httpCode + " 】:", e);
		} catch (IOException e) {
			throw new CheckException(
					"创建http getRequestWithByteArray请求发生异常【请求url:" + url
							+ ", httpCode:" + httpCode + " 】:", e);
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
			if(in !=null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(out !=null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return responseByteArray;
	}
    
	/**
	 * 模拟浏览器post方法，对url进行请求
	 * @param url		请求url(必填)
	 * @param params	需要post提交的参数(可选)
	 * @return 请求结果
	 */
	public static String postRequest(String url, Map<String, String> params) {
		PostMethod httppost = new PostMethod(url);
		if (params != null && params.size() > 0) {
			Iterator<String> keySet = params.keySet().iterator();
			NameValuePair[] nameValuePair = new NameValuePair[params.size()];

			for (int i = 0; keySet.hasNext(); i++) {
				String key = keySet.next();
				NameValuePair action = new NameValuePair(key, params.get(key));
				nameValuePair[i] = action;
			}
			httppost.setRequestBody(nameValuePair);
		}
		String responseBody = null;
		int httpCode = 0;
		try {
			httpCode = HTTP_CLIENT.executeMethod(httppost);
			responseBody = new String(httppost.getResponseBody());
		} catch (Exception e) {
			throw new SystemException("创建http postRequest请求发生异常【请求url:" + url
					+ ", httpCode:" + httpCode + " 】:", e);
		} finally {
			if (httppost != null) {
				httppost.releaseConnection();
			}
		}
		return responseBody;
	}
	

}
