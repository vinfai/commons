/**
 * 
 */
package com.shengpay.commons.core.base;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.utils.ClassUtils;

/**
 * 对象信息整理器
 * @author lindongcheng
 *
 */
public class ObjectInfoBuilder {
	private static final int MAX_LOG_ITEM = 5;

	/**
	 * 在此包内的类型，在日志输出时需要深入
	 */
	private static List<String> packageList = new ArrayList<String>();
	static {
		packageList.add("com.shengpay.");
		packageList.add("com.sdp.");
		packageList.add("com.snda.");
		packageList.add("com.sdo.");
	}

	private List<Object> alreadBuildObjList;
	
	private Object obj;
	
	public ObjectInfoBuilder(Object obj) {
		this.obj=obj;
	}
	
	public String getObjectInfo() {
		if(obj==null) {
			return "null";
		}

		init();
		return "{" + obj.getClass().getName() + "@" + Integer.toHexString(obj.hashCode()) + "[" + getClassInfo(obj, obj.getClass()) + "]｝";
	}

	private void init() {
		alreadBuildObjList=new ArrayList<Object>();
		alreadBuildObjList.add(obj);
	}

	@SuppressWarnings("rawtypes")
	private String getClassInfo(Object obj, Class theClass) {
		if (!inLogOutPackage(theClass)) {
			return objToSimpleString(obj);
		}

		if (theClass == null) {
			return "[class of value is null]";
		}

		StringBuffer classInfo = new StringBuffer();

		// 以此取得各个属性信息
		Field[] allFields = theClass.getDeclaredFields();
		for (int i = 0; i < allFields.length; i++) {
			Field aField = allFields[i];
			if (isFinalField(aField) || isStaticField(aField)) {
				continue;
			}

			LogOutAnn logOutAnn = aField.getAnnotation(LogOutAnn.class);
			if (logOutAnn != null && logOutAnn.disableLogOut()) {
				continue;
			}

			// 取得单个域的信息,若域信息为空时(常量域),则跳过该域
			String aFieldInfo = getSingleFieldInfo(obj, aField);
			if (aFieldInfo == null) {
				continue;
			}

			// 保持域信息
			classInfo.append(aFieldInfo);
			classInfo.append(";");
		}

		// 输出父类信息
		Class parentClass = theClass.getSuperclass();
		if(parentClass!=null && parentClass!=Object.class) {
			classInfo.append(getClassInfo(obj, parentClass));
		}

		return classInfo.toString();
	}

	private boolean inLogOutPackage(Object obj) {
		if(obj==null) {
			return false;
		}
		return inLogOutPackage(obj.getClass());
	}

	/**
	 * @param package1
	 * @return
	 */
	private boolean inLogOutPackage(Class<?> class1) {
		if(class1==null) {
			return false;
		}
		
		if(class1.isArray()) {
			class1=ClassUtils.getArrayOrigClass(class1);
		}
		Package package1 = class1.getPackage();
		if(package1==null) {
			return false;
		}
		
		String string = package1.getName();
		for (String packageName : packageList) {
			if (string.startsWith(packageName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 取得单个域详情(静态常量域返回空)
	 * 
	 * @param aField
	 * @return
	 */
	private String getSingleFieldInfo(Object obj, Field aField) {
		
		// 返回属性信息
		Object fieldValue = getFieldValue(obj, aField);
		String fieldName = aField.getName();
		return fieldName + "=" + getObjectLogInfo(fieldValue);
	}
	/**
	 * 取得指定对象指定域的值
	 * 
	 * @param obj
	 * @param aField
	 * @return
	 */
	private Object getFieldValue(Object obj, Field aField) {
		// 若当前域为常量域,则返回空信息
		if (isConstantsField(aField)) {
			return null;
		}

		// 获取域属性名和属性值
		Object fieldValue = null;
		try {
			aField.setAccessible(true);
			fieldValue = aField.get(obj);
		} catch (Exception e) {
			throw new SystemException("取得对象【" + obj + "】的域【" + aField + "】的值时发生异常：", e);
		}
		return fieldValue;
	}

	/**
	 * 判断一个域是否为常量域
	 * 
	 * @param aField
	 * @return
	 */
	private boolean isConstantsField(Field aField) {
		return isStaticField(aField) && isFinalField(aField);
	}

	/**
	 * 判断一个域是否为static域
	 * 
	 * @param aField
	 * @return
	 */
	private boolean isStaticField(Field aField) {
		return Modifier.isStatic(aField.getModifiers());
	}

	/**
	 * 判断一个域是否为Final域
	 * 
	 * @param aField
	 * @return
	 */
	private boolean isFinalField(Field aField) {
		return Modifier.isFinal(aField.getModifiers());
	}
	/**
	 * 取得对象的日志输出信息
	 * 
	 * @param obj
	 * @return
	 */
	public String getObjectLogInfo(Object obj) {
		if(this.alreadBuildObjList.contains(obj)) {
			return objToSimpleString(obj);
		}
		alreadBuildObjList.add(obj);

		
		// 空返回值情况
		if (obj == null) {
			return "null";
		}

		// 字符串返回值情况
		if (obj instanceof String) {
			return "\"" + obj + "\"";
		}

		// 列表返回值情况
		if (obj instanceof List<?>) {
			return getListLogInfo(((List<?>) obj));
		}

		// 映射返回值情况
		if (obj instanceof Map<?, ?>) {
			return getMapLogInfo(((Map<?, ?>) obj), 5);
		}
		
		if(obj.getClass().isArray()) {
			return getArrayLogInfo(obj);
		}

		// 不在关注包里的，不再深入
		if (!inLogOutPackage(obj)) {
			return String.valueOf(obj);
		}

		// 其他情况
		return objToString(obj);
	}
	private String getArrayLogInfo(Object obj) {
		if (obj == null) {
			return "null";
		}

		StringBuffer buf = new StringBuffer("{Array(length=" + Array.getLength(obj) + "):");
		for (int i = 0;i<Array.getLength(obj) && i < MAX_LOG_ITEM; i++) {
			buf.append((i> 0 ? "," : "") + objToString(Array.get(obj,i)) + "");
		}
		if (Array.getLength(obj)>MAX_LOG_ITEM) {
			buf.append(",...}");
		} else {
			buf.append("}");
		}

		return buf.toString();
	}

	private String objToSimpleString(Object obj) {
		if(obj==null) {
			return "null";
		}
		return obj.getClass().getName()+"@"+obj.hashCode();
	}
	/**
	 * 返回列表日志信息
	 * 
	 * @param listResult
	 * @param maxListLogItem
	 *            列表输出数量
	 * @return
	 */
	private String getMapLogInfo(Map<?, ?> listResult, int maxListLogItem) {
		if (listResult == null) {
			return "null";
		}

		int i = 0;
		StringBuffer buf = new StringBuffer("{Map(size=" + listResult.size() + "):");
		Iterator<?> iterator = null;
		for (iterator = listResult.entrySet().iterator(); iterator.hasNext() && i < maxListLogItem;) {
			buf.append((i++ > 0 ? "," : "") + objToString(iterator.next()) + "");
		}
		if (iterator.hasNext()) {
			buf.append(",...}");
		} else {
			buf.append("}");
		}

		return buf.toString();
	}

	private String objToString(Object obj) {
		if(obj==null) {
			return "null";
		}
		return "{" + obj.getClass().getName() + "@" + obj.hashCode() + "[" + getClassInfo(obj, obj.getClass()) + "]｝";
	}
	/**
	 * 返回列表日志信息
	 * 
	 * @param listResult
	 * @return
	 */
	private String getListLogInfo(List<?> listResult) {
		return getListLogInfo(listResult, 5);
	}

	/**
	 * 返回列表日志信息
	 * 
	 * @param listResult
	 * @param maxListLogItem
	 *            列表输出数量
	 * @return
	 */
	private String getListLogInfo(List<?> listResult, int maxListLogItem) {
		if (listResult == null) {
			return "null";
		}

		int i = 0;
		StringBuffer buf = new StringBuffer("{List(size=" + listResult.size() + "):");
		Iterator<?> iterator = null;
		for (iterator = listResult.iterator(); iterator.hasNext() && i < maxListLogItem;) {
			Object next = iterator.next();
			buf.append((i++ > 0 ? "," : "") + objToString(next) + "");
		}
		if (iterator.hasNext()) {
			buf.append(",...}");
		} else {
			buf.append("}");
		}

		return buf.toString();
	}

}
