/*
 * File: SdoExceptionMonitor
 * ProjectName: MCZ
 * Description: 
 * 
 * 
 * -----------------------------------------------------------
 * Date            Author          Changes
 * 2010-11-12         wuxin           created
 */
package com.shengpay.commons.springtools.aop;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

/** 
 * 功能:  当系统发生未处理的异常时通知监控 <p> 
 * 用法:
 * @version 1.0
 */

public class SdoExceptionMonitor {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SdoExceptionMonitor.class);
	
	private String notifyUrl;  // 监控系统的地址
	private String defaultType; // 默认通知类型
    private Map<String,String> exceptionMappings;  // 异常和通知类型的映射
	
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	
	public void setDefaultType(String defaultType) {
		this.defaultType = defaultType;
	}

	public void setExceptionMappings(Map<String,String> exceptionMappings) {
		this.exceptionMappings = exceptionMappings;
	}
	
	
	/**
	 * 通知监控部门
	 * @param ex
	 */
	public void reportToMonitor(Throwable ex) {
		
		String host = getLocalIP();
		
		String msg = "未知的异常信息";
		try {
			if ( ex != null )
			     msg = URLEncoder.encode(ex.getMessage() == null? ex.getClass().getName():ex.getMessage(),"GBK");
		} catch (UnsupportedEncodingException e) {
			logger.warn("reportToMonitor(Throwable ex) - exception ignored", e);
		}
		
		// 通知监控 
		sendRequest(buildUrl(host, msg,ex));
		
		
	}
	
	/**
	 * 获取服务器IP地址
	 * @return
	 */
	private  String getLocalIP() {
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
			logger.error("getLocalIP()", e);
            return null;
        }
        
        byte[] ipAddr = addr.getAddress();
        String ipAddrStr = "";
        for (int i = 0; i < ipAddr.length; i++) {
        	System.out.println(ipAddr[i]);
            if (i > 0) {
                ipAddrStr += ".";
            }
            ipAddrStr += ipAddr[i] & 0xFF;
        }
        return ipAddrStr;
    }
	/**
	 * @param host
	 * @param msg
	 * @param ex
	 * @return
	 */
	private String buildUrl(String host, String msg,Throwable ex){
		 StringBuffer buf = new StringBuffer();
		 buf.append(notifyUrl)
			.append("?type=")
			.append(findMatchingType(ex))
			.append("&host=")
			.append(host)
			.append("&msg=")
			.append(msg);
		return buf.toString(); 
	}
	/**
	 * 查找通知类型和异常的映射关系
	 * @param ex
	 * @return
	 */
	private String findMatchingType(Throwable ex){
		if (exceptionMappings == null){
			return defaultType;
		}
		String result = exceptionMappings.get(ex.getClass().getName());
		if (result == null || result.length() == 0){
			result = defaultType;
		}
		return result;
	}
	
	/**
	 * 发送通知
	 * @param url
	 * @return
	 */
	private  String sendRequest(String url){
		HttpClient client = new HttpClient();
		client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "GBK");
		
		String reponse  = null;
		GetMethod method = new GetMethod(url);
		try {
			int returnCode = client.executeMethod(method);

			if (returnCode != HttpStatus.SC_OK) {
				// 请求出错
				logger.error("发送HTTP请求出错,响应代码: " + returnCode + " 用户行为内容: " + url);
			}

		} catch (Exception e) {
			logger.error("发送HTTP请求出错,错误原因: " + e.getMessage() + " 用户行为内容: " + url);
		} finally {
			method.releaseConnection();
		}

		return reponse;
	}

	
}
