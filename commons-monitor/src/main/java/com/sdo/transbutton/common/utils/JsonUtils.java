package com.sdo.transbutton.common.utils;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdo.transbutton.common.exception.BusinessException;
import com.sdo.transbutton.common.exception.SystemException;

/**
 * json对象转换器
 * @description	
 * @usage		
 * @copyright	Copyright 2009 5173 Corporation. All rights reserved.
 * @company		5173 Corporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: GsonHandler.java,v 1.0 2010-12-2 上午10:23:02 lindongcheng Exp $
 * @create		2010-12-2 上午10:23:02
 */
public class JsonUtils {

	/**
	 * gson对象转换器
	 */
	private static Gson gson = new GsonBuilder().create();

	/**
	 * GSON对象分隔符
	 */
	private static final String GSON_SPLIT_OBJ_END = "\r";

	/**
	 * GSON对象分隔符
	 */
	private static final String GSON_SPLIT_TYPE_OBJ = "\0";

	/**
	 * 将Object数组转换成Gson对象
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
			buf.append(ClassUtils.getObjType(aObj) + GSON_SPLIT_TYPE_OBJ + getJsonStr(aObj));
		}

		return buf.toString();
	}

	/**
	 * 将对象转换为gson字符串
	 * @param aObj
	 * @return
	 */
	private static String getJsonStr(Object aObj) {
		//空指针情况
		if(aObj==null){
			return ClassUtils.CLASS_NAME_NULL;
		}
		
		//异常信息情况
		if(aObj instanceof Throwable){
			return ThrowableUtils.getRootMessage((Throwable)aObj);
		}
		
		//其他对象情况
		return gson.toJson(aObj);
	}

	/**
	 * 将Gson对象转换为Object数组
	 * @param gsonStr
	 * @return
	 */
	public static Object[] fromJson(String gsonStr) {
		String[] objsGson = gsonStr.split(GSON_SPLIT_OBJ_END);
		Object[] objs = new Object[objsGson.length];
		for (int i = 0; i < objsGson.length; i++) {
			String aObjGson = objsGson[i];
			String[] objUnionInfo = aObjGson.split(GSON_SPLIT_TYPE_OBJ);
			String typeInfo = objUnionInfo[0];
			String objInfo = objUnionInfo[1];

			if (ClassUtils.CLASS_NAME_NULL.equals(typeInfo)) {
				objs[i] = null;
				continue;
			}
			
			try {
				Class<?> objClass = Class.forName(typeInfo);
				if(ClassUtils.asSubclass(objClass,RuntimeException.class)){
					objs[i]=new RuntimeException(objInfo);
					continue;
				}
				
				if(ClassUtils.asSubclass(objClass,Exception.class)){
					objs[i]=new Exception(objInfo);
					continue;
				}
				
				objs[i] = gson.fromJson(objInfo, objClass);
			} catch (Exception e) {
				throw new SystemException("", e);
			}
		}

		return objs;
	}

	/**
	 * 测试用main方法
	 * @param args
	 */
	public static void main(String[] args) {
		Object[] obj=new Object[]{"asdfas",new Date(),true,1,new Long(15),new JsonUtils(),new RuntimeException("运行时异常"),new Exception("普通异常"),null};
		
		String gsonStr = toJson(obj);
		System.out.println(gsonStr);
		
		Object[] toObjs = fromJson(gsonStr);
		for (Object object : toObjs) {
			System.out.println(ClassUtils.getObjType(object)+":"+object);
		}
	}
}
