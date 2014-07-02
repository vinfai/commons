package com.shengpay.commons.core.tools;

import com.shengpay.commons.core.base.BaseObject;
import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.utils.StringUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class UrlString extends BaseObject {

	private String url;

	private Map<String, String> paramMap = new LinkedHashMap<String, String>();

	public static UrlString createByValuesMap(String aUrlString, Map<String, Object> valuesMap) {
		return new UrlString(aUrlString,null,valuesMap);
	}
	
	public UrlString(String aUrlString, Map<String, String> aParamsMap, Map<String, Object> valuesMap) {
		setUrl(aUrlString);
		addParamMap(aParamsMap);
		setValueMap(valuesMap);
	}

	public void setValueMap(Map<String, Object> valueMap) {
		for (Entry<String, String> paramEntry : paramMap.entrySet()) {
			String value = StringUtils.convertPlaceholder(paramEntry.getValue(), valueMap);
			addParameter(paramEntry.getKey(),value);
		}
	}

	public UrlString(String aUrlString, Map<String, String> paramsMap) {
		this(aUrlString, paramsMap, null);
	}

	public UrlString(String aUrlString) {
		this(aUrlString, (Map<String,String>)null);
	}

	public void addParameter(String paramName, String paramValue) {
		if (StringUtils.isBlank(paramName)) {
			throw new SystemException("URL参数名不能为空！");
		}

		paramMap.put(paramName, paramValue);
	}

	public String toString() {
		if (paramMap == null || paramMap.size() == 0) {
			return url;
		}

		if(url==null) return getParamsString();
		
		if (url.indexOf("?") >= 0) {
			return url + "&" + getParamsString();
		} else {
			return url + "?" + getParamsString();
		}
	}

	private String getParamsString() {
		if (paramMap.size() == 0) {
			return "";
		}

		StringBuffer buf = new StringBuffer();
		int i = 0;
		for (Entry<String, String> paramEntry : paramMap.entrySet()) {
			String paramString = getParamString(paramEntry.getKey(), paramEntry.getValue());
			buf.append(i++ == 0 ? paramString : "&" + paramString);
		}
		return buf.toString();
	}

	public static Map<String, String> parseParams(String paramsString) {
		if (StringUtils.isBlank(paramsString)) {
			return null;
		}

		String[] paramString = paramsString.split("&");
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (String string : paramString) {
			String[] param = string.split("=");
			String key = param.length > 0 ? param[0] : null;
			String value = param.length > 1 ? param[1] : null;
			map.put(key, value);
		}

		return map;
	}

	public static String getParamValue(String paramsString, String paramName) {
		return parseParams(paramsString).get(paramName);
	}

	private String getParamString(String paramName, Object paramValue) {
		return paramName + "=" + getParamValueString(paramValue);
	}

	private String getParamValueString(Object paramValue) {
		if (paramValue == null) {
			return "";
		}
		try {
			return URLEncoder.encode(paramValue.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new SystemException("对【" + paramValue + "】进行URL编码时发生异常:", e);
		}
	}

	public String readResponseMsg() {
		PostMethod pm = new PostMethod(url);
		pm.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		for (Entry<String, String> param : paramMap.entrySet()) {
			pm.addParameter(param.getKey(), param.getValue());
		}

		HttpClient hc = new HttpClient();
		try {
			hc.executeMethod(pm);
//			if (statusCode != HttpStatus.SC_OK) {
//				throw new SystemException("访问【" + this + "】取得的应答码【" + statusCode + "】异常,内容为【" + responseBody + "】！");
//			}
			return new String(pm.getResponseBody());
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException("访问【" + this + "】时发生异常：", e);
		}
	}

	/**
	 * @return the params
	 */
	public Map<String, String> getParamMap() {
		return Collections.unmodifiableMap(paramMap);
	}

	public static void main(String[] args) {
		UrlString url = new UrlString("https://api.weibo.com/2/statuses/update.json");
		url.addParameter("access_token", "2.00vEiDBCqzKk9Dcdbc25db334kasYB");
		url.addParameter("status", "测试发微博");
		System.out.println(url);
		System.out.println(url.readResponseMsg());
	}

	public static void main2(String[] args) {
		UrlString url = new UrlString("https://open.t.qq.com/api/t/add");
		url.addParameter("oauth_consumer_key", "6e7e5559b1fc4710b54c5ad6b962d801");
		url.addParameter("access_token", "81231bb69927f8cd325d7f1acdf2e30b");
		url.addParameter("openid", "00000000000000000000000003D48C39");
		url.addParameter("oauth_version", "2.a");

		url.addParameter("format", "json");
		url.addParameter("content", "微博内容33333！");
		System.out.println(url);
		System.out.println(url.readResponseMsg());
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String aUrlString) {
		if (StringUtils.isBlank(aUrlString)) {
			throw new SystemException("URL地址不能为空！");
		}
		
		if(!aUrlString.toLowerCase().startsWith("http")){
			paramMap=parseParams(aUrlString);
			return;
		}
		
		int indexOf = aUrlString.indexOf('?');
		if(indexOf<0) {
			this.url = aUrlString;
			return;
		}
		
		url=aUrlString.substring(0,indexOf);
		paramMap=parseParams(aUrlString.substring(indexOf+1));
	}

	/**
	 * @param paramMap the paramMap to set
	 */
	public void addParamMap(Map<String, String> paramMap) {
		if(paramMap==null || paramMap.size()==0) {
			return;
		}

		this.paramMap.putAll(paramMap);
	}
	
}
