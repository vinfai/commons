package com.shengpay.commons.web.util;

import java.util.List; 
import net.sf.json.JSONArray;  
import net.sf.json.JSONObject;

/**
 * json method 工具方法
 * @description	 
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDO Corporation.
 * @author 		wangjinhua
 * @version		$Id: UserLoginCtrl.java,v 1.2 2009-12-4 下午02:31:10 chenjianxiao Exp $
 * @create		2009-12-4 下午02:31:10
 *
 */
public class JSONUtil {
	
	/**
	 * object转换成JSONArray字符串
	 * @param obj   
	 * @return string
	 */
	public static String obj2json(Object obj){
		JSONArray jsonArray2 = JSONArray.fromObject(obj);
	    return jsonArray2.toString();
	}
	
	/**
	 * JSONArray字符串转换成List
	 * @param json  JSONArray字符串
	 * @param clazz List中存放的对象的Class  
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> json2list(String json,Class<T> clazz){
		JSONArray jsonArray = JSONArray.fromObject(json);
		return (List<T>)JSONArray.toCollection(jsonArray, clazz);
	}
	
	/**
	 * object转换成JSON字符串
	 * @param obj   
	 * @return string
	 */
	public static String obj2jsonStr(Object obj){
		JSONObject jsonObject = JSONObject.fromObject(obj);  
		return jsonObject.toString();
		
	}
}
