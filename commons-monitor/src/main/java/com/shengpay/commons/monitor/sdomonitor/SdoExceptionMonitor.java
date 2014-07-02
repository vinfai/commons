package com.shengpay.commons.monitor.sdomonitor;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;

import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.utils.ThrowableUtils;
import com.shengpay.commons.monitor.utils.IPUtils;

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
	
    private String ipAddress;
    
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	
	public void setDefaultType(String defaultType) {
		this.defaultType = defaultType;
	}

	public void setExceptionMappings(Map<String,String> exceptionMappings) {
		this.exceptionMappings = exceptionMappings;
	}
	
	{
		try {
			ipAddress = IPUtils.getServerIPsStr();
		} catch (Exception e) {
			throw new SystemException("获取ip地址出错", e);
		}
	}
	
	
	/**
	 * 通知监控部门
	 * @param ex
	 */
	public void reportToMonitor(String moduleName, Throwable ex) {
		String host = ipAddress;
		String msg = "未知的异常信息";
		try {
			if (ex != null) {
				String exmsg = ThrowableUtils.getRootMessage(ex);
				msg = StringUtils.isBlank(exmsg) ? ex.getClass().getName() : exmsg;
			}
			msg = moduleName + msg;
			msg = URLEncoder.encode(msg, "GBK");
			host = URLEncoder.encode(host, "GBK");
		} catch (UnsupportedEncodingException e) {
			logger.warn("reportToMonitor(Throwable ex) - exception ignored", e);
		}

		// 通知监控
		sendRequest(buildUrlShengpay(host, msg, ex));
	}
	
	/**
	 * 获取服务器IP地址
	 * @return
	 */
	@SuppressWarnings("unused")
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
	@SuppressWarnings("unused")
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
	
	private String buildUrlShengpay(String host, String msg, Throwable ex){
		 StringBuffer buf = new StringBuffer();
		 buf.append(notifyUrl)
		 	.append("?sourceid=13&devicetype=1&deviceid=111&ip=")
		 	.append(host)
		 	.append("&happentime=").append(DateFormatUtils.format(Calendar.getInstance(), "yyyyMMddHHmmss"))
		 	.append("&errcode=-1&errdesc=").append(msg)
		 	.append("&alarmtype=1&alarmlevel=1&principal=yechunlin");
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
