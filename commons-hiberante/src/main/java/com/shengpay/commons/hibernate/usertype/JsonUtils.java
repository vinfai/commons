package com.shengpay.commons.hibernate.usertype;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.shengpay.commons.core.utils.ClassUtils;
import com.shengpay.commons.core.utils.StringUtils;
import com.shengpay.commons.core.utils.ThrowableUtils;

/**
 * json对象转换器
 * 
 * @description
 * @usage
 * @copyright Copyright 2009 5173 Corporation. All rights reserved.
 * @company 5173 Corporation.
 * @author LinDongCheng <lindongcheng@snda.com>
 * @version $Id: GsonHandler.java,v 1.0 2010-12-2 上午10:23:02 lindongcheng Exp $
 * @create 2010-12-2 上午10:23:02
 */
public class JsonUtils {

	/**
	 * gson对象转换器
	 */
//	private static Gson gson;
//	static {
//		GsonBuilder gsonBuilder = new GsonBuilder();
//		gsonBuilder.registerTypeAdapter(Map.class, new NaturalDeserializer());
//		gson=gsonBuilder.create();
//	}

	/**
	 * GSON对象分隔符
	 */
	private static final String GSON_SPLIT_OBJ_END = "\0";

	public static String toJson(Object obj) {
		return toJson(new Object[] { obj });
	}

	/**
	 * 将Object数组转换成Gson对象
	 * 
	 * @param objs
	 * @return
	 */
	public static String toJson(Object[] objs) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < objs.length; i++) {
			if (i > 0) {
				buf.append(GSON_SPLIT_OBJ_END);
			}
			Object aObj = objs[i];
			buf.append(getJsonStr(aObj));
		}

		return buf.toString();
	}

	/**
	 * 将对象转换为gson字符串
	 * 
	 * @param aObj
	 * @return
	 */
	private static String getJsonStr(Object aObj) {
		// 异常信息情况
		if (aObj instanceof Throwable) {
			return ThrowableUtils.getRootMessage((Throwable) aObj);
		}

		if(aObj==null) {
			return "null";
		}

		// 其他对象情况
		return JSON.toJSONString(aObj);
	}

	public static Object fromJson(Class<?> classes, String gsonStr) {
		if(StringUtils.isBlank(gsonStr)) {
			return null;
		}
		
		Object[] fromJson = fromJson(new Class[] { classes }, gsonStr);
		return fromJson != null && fromJson.length >= 1 ? fromJson[0] : null;
	}

	/**
	 * 将Gson对象转换为Object数组
	 * 
	 * @param classes
	 * @param gsonStr
	 * @return
	 */
	public static Object[] fromJson(Class<?>[] classes, String gsonStr) {
		List<String> splitStringAccurate = StringUtils.splitStringAccurate(gsonStr, GSON_SPLIT_OBJ_END);
		Object[] objs = new Object[splitStringAccurate.size()];
		int i=0;
		for (String objInfo : splitStringAccurate) {
			Class<?> classOfT = classes[i];

			if (ClassUtils.asSubclass(classOfT, RuntimeException.class)) {
				objs[i] = new RuntimeException(objInfo);
				continue;
			}

			if (ClassUtils.asSubclass(classOfT, Exception.class)) {
				objs[i] = new Exception(objInfo);
				continue;
			}

			objs[i] = gson2Obj(objInfo, classOfT);
			i++;
		}

		return objs;
	}

	private static Object gson2Obj(String objInfo, Class<?> classOfT) {
		if("null".equals(objInfo)) {
			return null;
		}
		
		return JSON.parseObject(objInfo, classOfT);
	}


	public static void main(String[] args) {
		Map map=new HashMap();
		map.put("client_secret", "80ad077e097c16c813567483776cbc01");
		map.put("grant_type", "authorization_code");
		map.put("client_id", "6e7e5559b1fc4710b54c5ad6b962d801");
		String json = toJson(map);
		System.out.println(json);
		System.out.println(fromJson(Map.class, json));
	}

}

class BatchPayResponse {
	public BatchPayResponse(String resultCode, List<RowParseError> rowParseErrorList) {
		super();
		this.resultCode = resultCode;
		this.rowParseErrorList = rowParseErrorList;
	}

	String resultCode;
	List<RowParseError> rowParseErrorList;
}

class RowParseError {
	public RowParseError(int errorRow, String errorMsg) {
		super();
		this.errorRow = errorRow;
		this.errorMsg = errorMsg;
	}

	int errorRow;
	String errorMsg;

}