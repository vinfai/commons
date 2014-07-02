package com.shengpay.commons.springtools.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shengpay.commons.core.utils.ClassUtils;
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
	private static Gson gson = new GsonBuilder().create();

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

		// 其他对象情况
		return gson.toJson(aObj);
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromJson(Class<T> classes, String gsonStr) {
		Object[] fromJson = fromJson(new Class[] { classes }, gsonStr);
		return (T) (fromJson != null && fromJson.length >= 1 ? fromJson[0] : null);
	}

	/**
	 * 将Gson对象转换为Object数组
	 * 
	 * @param classes
	 * @param gsonStr
	 * @return
	 */
	public static Object[] fromJson(Class<?>[] classes, String gsonStr) {
		String[] objsGson = gsonStr.split(GSON_SPLIT_OBJ_END);
		Object[] objs = new Object[objsGson.length];
		for (int i = 0; i < objsGson.length; i++) {
			String objInfo = objsGson[i];
			Class<?> classOfT = classes[i];

			if (ClassUtils.asSubclass(classOfT, RuntimeException.class)) {
				objs[i] = new RuntimeException(objInfo);
				continue;
			}

			if (ClassUtils.asSubclass(classOfT, Exception.class)) {
				objs[i] = new Exception(objInfo);
				continue;
			}

			objs[i] = gson.fromJson(objInfo, classOfT);
		}

		return objs;
	}

	public static void main3(String[] args) {
		System.out.println(ClassUtils.asSubclass(String.class, Throwable.class));
	}

	public static void main2(String[] args) {
		Object ob = 1;
		String json = gson.toJson(ob);
		System.out.println(json);
		Object fromJson = gson.fromJson(json, ClassUtils.getClass(ob));
		System.out.println(fromJson);
	}

	public static void main(String[] args) {
		List<RowParseError> rowParseErrorList = new ArrayList<RowParseError>();
		rowParseErrorList.add(new RowParseError(15, "银行不支持"));
		rowParseErrorList.add(new RowParseError(25, "银行卡号错误"));
		rowParseErrorList.add(new RowParseError(15, "付款金额不合法"));
		BatchPayResponse aObj = new BatchPayResponse("0001", rowParseErrorList);
		System.out.println(getJsonStr(aObj));
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