/**
 * 
 */
package com.shengpay.commons.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.utils.StringUtils;

/**
 * HTTP协议相关的工具方法
 * @author Lincoln
 */

public class HttpUtils {
    private static Logger logger = Logger.getLogger(HttpUtils.class);

    /**
     * 取得HTTP请求的信息
     * 
     * @param request http请求
     * @return http请求的信息{格式(例):[ip=>http://www.172.com/a.htm?name=lin]}
     */
    @SuppressWarnings("unchecked")
    public static String getRequestInfo(HttpServletRequest request) {
        StringBuffer buf = new StringBuffer(request.getRemoteAddr() + "=>" + request.getRequestURL() + "?");
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Iterator<Entry<String, String[]>> ite = parameterMap.entrySet().iterator(); ite.hasNext();) {
            Entry<String, String[]> paramEntry = ite.next();
            String paramName = paramEntry.getKey();
            String[] paramValueArr = paramEntry.getValue();
            String paramValue = (paramValueArr.length > 0 ? paramValueArr[0] : "");

            buf.append(paramName + "=" + paramValue + (ite.hasNext() ? "&" : ""));
        }
        return buf.toString();
    }
    
    /**
     * 取得http请求的参数
     * @param request	http请求
     * @return	(例)name=lin&age=18
     */
    public static String getRequestParamString(HttpServletRequest request){
		Map<String, String[]> parameterMap = request.getParameterMap();
		if (null != parameterMap && !parameterMap.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (Iterator<Entry<String, String[]>> ite = parameterMap.entrySet().iterator(); ite
					.hasNext();) {
				Entry<String, String[]> paramEntry = ite.next();
				String paramName = paramEntry.getKey();
				String[] paramValueArr = paramEntry.getValue();
				String paramValue = (paramValueArr.length > 0 ? paramValueArr[0] : "");
				sb.append(paramName + "=" + paramValue + (ite.hasNext() ? "&" : ""));
			}
			return sb.toString();
		}
		return org.apache.commons.lang.StringUtils.EMPTY;
    }
    
    /**
     * 取得http请求的参数，带问号
     * @param request
     * @return	(例)?name=lin&age=18
     */
    public static String getRequestParamStringWithQuestionMark(HttpServletRequest request){
    	String param = getRequestParamString(request);
    	if(org.apache.commons.lang.StringUtils.isNotBlank(param)){
    		return "?" + param;
    	}
    	return org.apache.commons.lang.StringUtils.EMPTY;
    }

    /**
     * 构造HTTP请求字符串
     * 
     * @param onlinePaymentGetwayUrl
     * @param checkinOnlinePaymentParamMap
     * @return
     */
    public static String makeHttpRequestURL(String onlinePaymentGetwayUrl, Map<String, String> checkinOnlinePaymentParamMap) {
        return makeHttpRequestURL(onlinePaymentGetwayUrl, checkinOnlinePaymentParamMap, "UTF-8");
    }
    
    /**
     * 构造HTTP请求字符串, 使用GBK编码
     * 
     * @param onlinePaymentGetwayUrl
     * @param checkinOnlinePaymentParamMap
     * @return
     */
    public static String makeGBKHttpRequestURL(String onlinePaymentGetwayUrl, Map<String, String> checkinOnlinePaymentParamMap) {
    	return makeHttpRequestURL(onlinePaymentGetwayUrl, checkinOnlinePaymentParamMap, "GBK");
    }

    /**
     * 将map型参数信息转换为get方式的url信息
     * 
     * @param actionUrl 提交地址
     * @param paramMap 参数映射
     * @param charSet 进行URL编码的字符集（若为null，表示不进行URL编码）
     * @return get方式的url信息
     */
    public static String makeHttpRequestURL(String actionUrl, Map<String, String> paramMap, String charSet) {
        StringBuffer buf = null;
        if (actionUrl.indexOf("?") != -1) {
            buf = new StringBuffer(actionUrl + "&");
        } else {
            buf = new StringBuffer(actionUrl + "?");
        }
        for (Iterator<Entry<String, String>> ite = paramMap.entrySet().iterator(); ite.hasNext();) {
            Entry<String, String> paramEntry = ite.next();
            String paramName = paramEntry.getKey();
            String paramValue = paramEntry.getValue();
            if (!StringUtils.isBlank(charSet) && StringUtils.notBlank(paramValue)) {
                try {
                    paramValue = URLEncoder.encode(paramValue, charSet);
                } catch (UnsupportedEncodingException e) {
                    throw new SystemException("在将字符串【" + paramValue + "】转换为【" + charSet + "】字符集的URL编码时发生异常：", e);
                }
            }
            if (StringUtils.notBlank(paramValue)) {
                buf.append(paramName + "=" + paramValue + (ite.hasNext() ? "&" : ""));
            } else {
                buf.append(paramName + "=" + (ite.hasNext() ? "&" : ""));
            }
        }
        return buf.toString();
    } 

    /**
     * URL转码防止中文字符乱码
     * 
     * @param str
     * @return
     */
    public static String URLEncode2GBK(String str) {

        if (null == str)
            return null;

        String strRet = "";
        try {
            strRet = java.net.URLEncoder.encode(str, "GBK");
        } catch (Exception e) {
        	throw new SystemException("将字符串进行GBK url encode时发生异常", e);
        }
        return strRet;
    }
    
    /**
     * URL转码
     * 
     * @param str
     * @return
     */
    @Deprecated
    public static String URLEncode2UTF8(String str) {
    	if (null == str){
    		return null;
    	}
    	
    	String strRet = "";
    	try {
    		strRet = java.net.URLEncoder.encode(str, "UTF-8");
    	} catch (Exception e) {
    		throw new SystemException("将字符串进行UTF-8 url encode时发生异常", e);
    	}
    	return strRet;
    }

    /**
     * URL转码
     * 
     * @param str
     * @return
     */
    @Deprecated
    public static String URLDecode2UTF8(String str) {
    	if (null == str){
    		return null;
    	}
    	
    	String strRet = "";
    	try {
    		strRet = java.net.URLDecoder.decode(str, "UTF-8");
    	} catch (Exception e) {
    		throw new SystemException("将字符串进行UTF-8 url decode时发生异常", e);
    	}
    	return strRet;
    }
    
    

    /**
     * 获取当前Web服务器上下文URL (例如:http://192.168.0.245:9080/web/index.jsp ->
     * http://192.168.0.245:9080)
     * 
     * @param request
     * @return
     */
    public static String getContextUrl(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String requestURL = request.getRequestURL().toString();
        return requestURL.substring(0, requestURL.length() - requestURI.length());
    }

    /**
     * 获取当前Web服务器上下文URL (例如:http://192.168.0.245:9080/web/index.jsp ->
     * http://192.168.0.245:9080/web)
     * 
     * @param request
     * @return
     */
    public static String getContextUrlAndContextPath(HttpServletRequest request) {
        return getContextUrl(request) + request.getContextPath();
    }

    /**
     * 获取当前Web服务器上下文URL(如果协议名称为https将替换为http)
     * (例如:http://192.168.0.245:9080/web/index.jsp -> http://192.168.0.245:9080)
     * 
     * @param request
     * @return
     */
    public static String getContextUrlNotHttps(HttpServletRequest request) {
        String contextUrl = getContextUrl(request);
        return contextUrl.replaceFirst("https", "http");
    }

    /**
     * 获取当前Web服务器上下文URL(如果协议名称为https将替换为http)
     * (例如:https://192.168.0.245:9080/web/index.jsp ->
     * http://192.168.0.245:9080/web)
     * 
     * @param request
     * @return
     */
    public static String getContextUrlAndContextPathNotHttps(HttpServletRequest request) {
        String contextUrl = getContextUrlAndContextPath(request);
        return contextUrl.replaceFirst("https", "http");
    }

    public static void main(String[] args) {
    	System.out.println(getUrlExcludeParams("http://www.163.com?a=1&b=2"));
    }

	/**
	 * 将参数信息追加到URL上
	 * @param url
	 * @param paramName
	 * @param paramValue
	 * @return
	 */
	public static String appendParamToUrl(String url,String paramName,String paramValue) {
		if (url.contains("?")) {
			return url + "&" + paramName + "="+paramValue ;
		} else {
			return url + "?" + paramName + "="+paramValue ;
		}
	}

	/**
	 * 从URL中取得附加参数信息
	 * @param requestUrl
	 * @return
	 */
	public static Map<String, String> getAttachParamMapByUrl(String requestUrl) {
		//排除空的情况
		if(StringUtils.isBlank(requestUrl) || requestUrl.indexOf("?")==-1){
			return null;
		}
		
		Map<String,String> attachParamMap=new HashMap<String,String>();
		int questionMarkIndex = requestUrl.indexOf("?");
		String params = requestUrl.substring(questionMarkIndex+1);
		String[] paramArr = params.split("&");		
		for (int i = 0; i < paramArr.length; i++) {
			String[] paramEntry = paramArr[i].split("=");
			if(paramEntry.length!=2){
				throw new SystemException("无法从URL【"+requestUrl+"】中解析出附加参数信息！");
			}
			
			String paramName=paramEntry[0];
			String paramValue=paramEntry[1];
			attachParamMap.put(paramName, paramValue);
		}
		
		return attachParamMap;
	}
	
	/**
	 * 返回无参数信息的url
	 * @param url
	 * @return
	 */
	public static String getUrlExcludeParams(String url){
		//排除空的情况
		if(StringUtils.isBlank(url) || url.indexOf("?")==-1){
			return url;
		}
		
		return url.substring(0,url.indexOf("?"));
	}
}
