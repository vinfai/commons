package com.sdo.transbutton.common.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

import com.sdo.transbutton.common.exception.BusinessException;
import com.sdo.transbutton.common.exception.SystemException;
import com.sdo.transbutton.common.protocol.ProtocolFieldAnn;

/**
 * 类反射工具类
 * 
 * @description
 * @author Lincoln
 */
public class ClassUtils {
	/**
	 * 系统日志输出句柄
	 */
	private static Logger logger = Logger.getLogger(ClassUtils.class);

	/**
	 * 取得指定包下的所有类集合
	 * 
	 * @param packageName
	 * @return
	 */
	public static Set<Class<?>> getClassSetByPackageName(String packageName) {
		// 取得包含有指定包的所有URL集合
		packageName = packageName.replace('.', '/');
		Enumeration<URL> resources;
		try {
			resources = Thread.currentThread().getContextClassLoader().getResources(packageName);
		} catch (IOException e) {
			throw new SystemException("", e);
		}

		// 分别从文件夹或JAR中取得类集合
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		while (resources.hasMoreElements()) {
			URL url = resources.nextElement();

			logger.info("从路径【" + url + "】加载类信息！");
			String protocol = url.getProtocol();
			if ("jar".equals(protocol)) {
				try {
					getClassSetForJar(packageName, url, classSet);
				} catch (Exception e) {
					throw new SystemException("", e);
				}
			} else if ("file".equals(protocol)) {
				String filePath;
				try {
					filePath = URLDecoder.decode(url.getFile(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					throw new SystemException("", e);
				}
				findAndAddClassesInPackageByFile(packageName, filePath, classSet);
			}
		}
		return classSet;
	}

	/**
	 * @param packageName
	 * @param url
	 * @param classSet
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private static void getClassSetForJar(String packageName, URL url, Set<Class<?>> classSet) throws IOException, ClassNotFoundException {
		JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry jarEntry = entries.nextElement();
			String jarEntryName = jarEntry.getName();
			if (jarEntry.isDirectory()) {
				continue;
			}
			if (!jarEntryName.startsWith(packageName)) {
				continue;
			}
			if (!jarEntryName.endsWith(".class")) {
				continue;
			}
			String classFullName = jarEntryName.replace('/', '.').substring(0, jarEntryName.length() - 6);
			classSet.add(Class.forName(classFullName));
		}
	}

	/**
	 * 以文件的形式来获取包下的所有Class
	 * 
	 * @param packageName
	 * @param packagePath
	 * @param classSet
	 * @param recursive
	 * @param classes
	 */
	public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, Set<Class<?>> classSet) {
		// 获取此包的目录 建立一个File
		File dir = new File(packagePath);
		File[] dirfiles = dir.listFiles(new FileFilter() {
			// 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
			public boolean accept(File file) {
				return (file.isDirectory()) || (file.getName().endsWith(".class"));
			}
		});

		// 循环所有文件
		for (File file : dirfiles) {
			// 如果是目录 则继续扫描
			if (file.isDirectory()) {
				findAndAddClassesInPackageByFile(packageName + "/" + file.getName(), file.getAbsolutePath(), classSet);
			} else {
				// 如果是java类文件 去掉后面的.class 只留下类名
				String className = file.getName();
				String classNameForAll = packageName.replace('/', '.') + '.' + className.replaceAll(".class", "");
				try {
					// 添加到集合中去
					Class<?> aClass = Class.forName(classNameForAll);
					classSet.add(aClass);
				} catch (ClassNotFoundException e) {
				}
			}
		}
	}

	/**
	 * 返回指定方法的参数的KEY数组
	 * 
	 * @param method
	 * @return
	 */
	public static String[] getMethodParamsKey(Method method) {
		StringBuffer methodFullName = new StringBuffer(ClassUtils.getMethodFullName(method));

		Class<?>[] parameterTypes = method.getParameterTypes();
		for (int i = 0; i < parameterTypes.length; i++) {
			methodFullName.append((i == 0 ? "(" : ",") + parameterTypes[i].getName() + (i + 1 == parameterTypes.length ? ")" : ""));
		}

		String[] paramsKeyArr = new String[parameterTypes.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			paramsKeyArr[i] = methodFullName + ".param" + i + "(" + parameterTypes[i].getName() + ")";
		}
		return paramsKeyArr;
	}

	/**
	 * 取得指定类的位置信息
	 * 
	 * @param aclass
	 * @return
	 */
	public static URL getUrlByClass(Class<?> aclass) {
		if(aclass==null){
			return null;
		}
		
		ProtectionDomain protectionDomain = aclass.getProtectionDomain();
		if(protectionDomain==null){
			return null;
		}
		
		CodeSource codeSource = protectionDomain.getCodeSource();
		if(codeSource==null){
			return null;
		}
		
		return codeSource.getLocation();
	}

	/**
	 * 返回列表日志信息
	 * 
	 * @param listResult
	 * @return
	 */
	public static String getListLogInfo(List<?> listResult) {
		return getListLogInfo(listResult, 5);
	}

	/**
	 * 取得对象的日志输出信息
	 * 
	 * @param obj
	 * @return
	 */
	public static String getObjectLogInfo(Object obj) {
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
			return getListLogInfo((List<?>) ((List<?>) obj));
		}

		// 其他情况
		return String.valueOf(obj);
	}

	/**
	 * 返回列表日志信息
	 * 
	 * @param listResult
	 * @param maxListLogItem
	 *            列表输出数量
	 * @return
	 */
	public static String getListLogInfo(List<?> listResult, int maxListLogItem) {
		if (listResult == null) {
			return "null";
		}

		int i = 0;
		StringBuffer buf = new StringBuffer("{List(size=" + listResult.size() + "):");
		Iterator<?> iterator = null;
		for (iterator = listResult.iterator(); iterator.hasNext() && i < maxListLogItem;) {
			buf.append((i++ > 0 ? "," : "") + iterator.next() + "");
		}
		if (iterator.hasNext()) {
			buf.append(",...}");
		} else {
			buf.append("}");
		}

		return buf.toString();
	}

	/**
	 * 获取调用信息;
	 * 
	 * @param mi
	 * @return
	 */
	public static String getMethodCallInfo(MethodInvocation mi) {
		return getMethodCallInfo(mi.getThis(),mi.getMethod(), mi.getArguments());
	}

	/**
	 * 获取方法调用信息
	 * @param method
	 * @param args
	 * @return
	 */
	public static String getMethodCallInfo(Method method,Object[] args) {
		return getMethodCallInfo(null, method, args);
	}
	
	/**
	 * 
	 * @param targetObj
	 * @param method
	 * @param args
	 * @return
	 */
	public static String getMethodCallInfo(Object targetObj,Method method,Object[] args) {
		String getMethodName = getMethodSimpleName(targetObj,method);
		StringBuffer callInfo = new StringBuffer();
		callInfo.append(getMethodName + "(");
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				callInfo.append(i > 0 ? "," : "");
				callInfo.append(getObjectLogInfo(args[i]));
			}
		}
		callInfo.append(")");
		return callInfo.toString();
	}

	/**
	 * 获取方法签名信息(例如:test(java.lang.String,java.lang.Long))
	 * 
	 * @param mi
	 * @return
	 */
	public static String getMethodSign(Method method) {
		// 参数信息
		StringBuffer callInfo = new StringBuffer();
		callInfo.append(method.getName() + "(");
		Class<?>[] parameterTypes = method.getParameterTypes();
		for (int i = 0; i < parameterTypes.length; i++) {
			callInfo.append(i > 0 ? "," : "");
			callInfo.append(parameterTypes[i].getName());
		}
		callInfo.append(")");
		return callInfo.toString();
	}

	/**
	 * 取得完整的方法签名
	 * 
	 * @param method
	 *            (例如:com.sdo.transbutton.common.proxyfactroy.JmsClientProxy.test
	 *            (java.lang.String,java.lang.Long))
	 * @return
	 */
	public static String getMethodSignFull(Method method) {
		return method.getDeclaringClass().getName() + "." + getMethodSign(method);
	}

	/**
	 * 取得指定类型所有方法的签名列表
	 * 
	 * @param cla
	 * @return
	 */
	public static List<String> getMethodSignList(Class<?> cla) {
		Method[] methods = cla.getDeclaredMethods();
		List<String> methodSignList = new ArrayList<String>();
		for (Method method : methods) {
			methodSignList.add(getMethodSign(method));
		}
		return methodSignList;
	}

	/**
	 * 取得指定类型所有方法对应接口列表的映射
	 * 
	 * @param cla
	 * @return<方法签名,接口列表>
	 */
	public static Map<String, List<Class<?>>> getMethod2InterfaceMap(Class<?> cla) {
		Type[] genericInterfaces = cla.getGenericInterfaces();
		Map<String, List<Class<?>>> interfaceMethodSignMap = new HashMap<String, List<Class<?>>>();

		for (int i = 0; i < genericInterfaces.length; i++) {
			Class<?> gi = (Class<?>) genericInterfaces[i];
			Method[] dms = gi.getDeclaredMethods();
			for (Method method : dms) {
				String methodSign = getMethodSign(method);
				List<Class<?>> classList = interfaceMethodSignMap.get(methodSign);
				if (classList == null) {
					classList = new ArrayList<Class<?>>();
					interfaceMethodSignMap.put(methodSign, classList);
				}
				classList.add(gi);
			}
		}

		return interfaceMethodSignMap;
	}

	/**
	 * 取得方法名称(例:ClassUtils.getMethodName)
	 * 
	 * @param mi
	 * @return
	 */
	public static String getMethodSimpleName(MethodInvocation mi) {
		Method method = mi.getMethod();
		return getMethodSimpleName(mi.getThis(),method);
	}

	public static String getMethodSimpleName(Method method) {
		return getMethodSimpleName(null,method);
	}
	
	/**
	 * 取得方法简称(所属类无包名)
	 * @param targetObj
	 * @param method
	 * @return
	 */
	public static String getMethodSimpleName(Object targetObj,Method method) {
		String callClassName = targetObj!=null?targetObj.getClass().getSimpleName():method.getDeclaringClass().getSimpleName();// 所属类名
		String callMethodName = method.getName();// 方法名称
		return callClassName + "." + callMethodName;
	}

	/**
	 * 取得一个方法的完整名称
	 * 
	 * @param mi
	 * @return
	 */
	public static String getMethodFullName(MethodInvocation mi) {
		Method method = mi.getMethod();
		return getMethodFullName(method);
	}

	/**
	 * 取得一个方法的完整名称
	 * 
	 * @param method
	 * @return
	 */
	public static String getMethodFullName(Method method) {
		return method.getDeclaringClass().getName() + "." + method.getName();
	}

	/**
	 * 取得特定类型字段属性的类型
	 * 
	 * @param aimClass
	 * @param propertyName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Class getPropertyClass(Class aimClass, String propertyName) {
		// 验证参数合法性
		if (aimClass == null || propertyName == null) {
			throw new SystemException("参数不合法[getPropertyClass(Class " + aimClass + ", String " + propertyName + ")]");
		}

		// 取得指定属性SET方法的名称
		String setMethodName = getSetMethodName(propertyName);

		// 查找类型中与SET方法名称相同的方法列表
		Method[] methods = aimClass.getMethods();
		List<Method> setMethodList = new ArrayList<Method>();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			String methodName = method.getName();
			if (methodName.equals(setMethodName)) {
				setMethodList.add(method);
			}
		}

		// 判断未找到的情况
		if (setMethodList.size() == 0) {
			throw new SystemException("类型[" + aimClass + "]中名称为[" + setMethodName + "]的方法未找到,无法判断属性[" + propertyName + "]的类型为什么");
		}

		// 判断找到多个的情况
		if (setMethodList.size() > 1) {
			throw new SystemException("类型[" + aimClass + "]中名称为[" + setMethodName + "]的方法多于1个,无法判断属性[" + propertyName + "]的类型为什么!");
		}

		// 判断无参数的情况
		Class<?>[] parameterTypes = setMethodList.get(0).getParameterTypes();
		if (parameterTypes == null) {
			throw new SystemException("类型[" + aimClass + "]中名称为[" + setMethodName + "]的方法没有参数,无法判断属性[" + propertyName + "]的类型为什么!");
		}

		// 判断有多个参数的情况
		if (parameterTypes.length > 1) {
			throw new SystemException("类型[" + aimClass + "]中名称为[" + setMethodName + "]的方法有多个参数[" + StringUtils.toString(parameterTypes) + "],无法判断属性[" + propertyName + "]的类型为什么!");
		}

		// 最终返回属性类型
		return parameterTypes[0];
	}

	/**
	 * 获取一个属性的get方法的名称
	 * 
	 * @param propertyName
	 * @return
	 */
	public static String getGetMethodName(String propertyName) {
		// 验证参数合法性
		if (StringUtils.isBlank(propertyName)) {
			throw new SystemException("无法取得属性[" + propertyName + "]对应的get方法的名称!");
		}

		// 取得首支付大写形式
		String firstChar = String.valueOf(propertyName.charAt(0)).toUpperCase();
		return "get" + firstChar + propertyName.substring(1);
	}

	/**
	 * 获取一个属性的get方法的名称
	 * 
	 * @param propertyName
	 * @return
	 */
	public static String getSetMethodName(String propertyName) {
		// 验证参数合法性
		if (StringUtils.isBlank(propertyName)) {
			throw new SystemException("无法取得属性[" + propertyName + "]对应的get方法的名称!");
		}

		// 取得首支付大写形式
		String firstChar = String.valueOf(propertyName.charAt(0)).toUpperCase();
		return "set" + firstChar + propertyName.substring(1);
	}

	public static void main2(String[] args) {
		Method[] methods = ClassUtils.class.getMethods();
		for (Method method : methods) {
			System.out.println(method.getName());
		}
	}

	/**
	 * 转化Object类型为String类型
	 * 
	 * @param objValue
	 * @param field
	 * @return
	 * @throws BusinessException
	 */
	public static String convertObject2String(Object objValue, Field field) throws BusinessException {
		if (objValue == null) {
			return "";
		}

		if (objValue instanceof Date) {
			ProtocolFieldAnn dateFormatAnn = field.getAnnotation(ProtocolFieldAnn.class);
			if (dateFormatAnn == null) {
				throw new SystemException("转换字符串为日期类型时无法取得日期格式信息！");
			}

			String dateFormat = dateFormatAnn.dateFormat();
			return DateTimeUtils.formateDate2Str((Date) objValue, dateFormat);

		}

		return String.valueOf(objValue);
	}

	/**
	 * 将字符串值转换为指定域所属类型
	 * 
	 * @param strValue
	 * @param field
	 * @return
	 * @throws BusinessException
	 */
	public static Object convertString2Obj(String strValue, Field field) throws BusinessException {
		if (StringUtils.isBlank(strValue)) {
			return null;
		}

		Class<?> fieldType = field.getType();
		if (fieldType == Date.class) {
			ProtocolFieldAnn dateFormatAnn = field.getAnnotation(ProtocolFieldAnn.class);
			if (dateFormatAnn == null) {
				throw new SystemException("转换字符串为日期类型时无法取得日期格式信息！");
			}

			String dateFormat = dateFormatAnn.dateFormat();
			return DateTimeUtils.parseDateByString(strValue, dateFormat);
		}

		return convertString2Obj(strValue, fieldType);
	}

	/**
	 * 转换String值为指定类型值
	 * 
	 * @param strValue
	 * @param aimClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object convertString2Obj(String strValue, Class aimClass) throws BusinessException {
		// 验证参数合法
		if (aimClass == null) {
			throw new SystemException("参数不合法[convertString2Obj(String " + strValue + ", Class " + aimClass + ")]");
		}

		// 排除空的情况
		if (StringUtils.isBlank(strValue)) {
			return null;
		}

		// String->String
		if (aimClass == String.class) {
			return strValue;
		}

		// String->Long
		if (aimClass == Long.class) {
			if (!NumberUtils.isLong(strValue)) {
				throw new BusinessException("bc.commons.lincoln.5", strValue);
			}

			return Long.parseLong(strValue);
		}

		// String -> Integer
		if (aimClass == Integer.class) {
			if (!NumberUtils.isInteger(strValue)) {
				throw new BusinessException("bc.commons.lincoln.7", strValue);
			}

			return Integer.parseInt(strValue);
		}

		// String->BigDecimal
		if (aimClass == BigDecimal.class) {
			if (!NumberUtils.isDouble(strValue)) {
				throw new BusinessException("bc.commons.lincoln.6", strValue);
			}

			return new BigDecimal(strValue);
		}

		// String -> Boolean
		if (aimClass == Boolean.class) {
			return "true".equals(strValue.toLowerCase()) ? Boolean.TRUE : Boolean.FALSE;
		}

		throw new SystemException("意料之外的目标类型[" + aimClass + "]");
	}

	/**
	 * 取得指定对象指定域的值
	 * 
	 * @param obj
	 * @param aField
	 * @return
	 */
	public static Object getFieldValue(Object obj, Field aField) {
		// 若当前域为常量域,则返回空信息
		boolean isConstantsField = isConstantsField(aField);
		if (isConstantsField) {
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
	 * @param aField
	 * @return
	 */
	public static boolean isConstantsField(Field aField) {
		return isStaticField(aField) && isFinalField(aField);
	}

	/**
	 * 判断一个域是否为Final域
	 * @param aField
	 * @return
	 */
	public static boolean isFinalField(Field aField) {
		int modifiers2 = aField.getModifiers();
		boolean isFinalField = Modifier.isFinal(modifiers2);
		return isFinalField;
	}

	/**
	 * 判断一个域是否为static域
	 * @param aField
	 * @return
	 */
	public static boolean isStaticField(Field aField) {
		int modifiers = aField.getModifiers();
		boolean isStaticField = Modifier.isStatic(modifiers);
		return isStaticField;
	}

	/**
	 * 获取域的类型的泛型的类型
	 * 
	 * @param field
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public static Class<?> getFieldActualType(Field field) {
		Type genericType = field.getGenericType();
		if (!(genericType instanceof ParameterizedType)) {
			throw new SystemException("域【" + field + "】的类型不支持泛型！");
		}

		Type[] types = ((ParameterizedType) genericType).getActualTypeArguments();
		if (types.length > 1) {
			throw new SystemException("域【" + field + "】的类型具有多个泛型信息！");
		}

		return (Class<?>) types[0];
	}

	/**
	 * 将字符串值设置到特定对象的特定属性中
	 * 
	 * @param aimObj
	 *            目标对象
	 * @param field
	 *            目标属性
	 * @param fieldValue
	 *            被设置的值
	 * @throws BusinessException
	 */
	public static void setStringValueToField(Object aimObj, Field field, String fieldValue) throws BusinessException {
		Object filedValue = ClassUtils.convertString2Obj(fieldValue, field);
		setObjectValueToField(aimObj, field, filedValue);
	}

	/**
	 * 将Object值设置到特定对象的特定属性中
	 * 
	 * @param aimObj
	 * @param field
	 * @param filedValue
	 */
	public static void setObjectValueToField(Object aimObj, Field field, Object filedValue) {
		try {
			if (filedValue != null) {
				field.setAccessible(true);
				field.set(aimObj, filedValue);
			}
		} catch (Exception e) {
			throw new SystemException("将属性[" + field.getName() + "]的值[" + filedValue + "]设置到对象[" + aimObj + "]时发生异常", e);
		}
	}

	/**
	 * 使用指定类加载器克隆一个目标对象
	 * @param srcObj
	 * @param targetClass
	 * @return
	 */
	public static Object cloneObjectByClassLoad(Object srcObj,Class<?> targetClass){
		try {
			Object newInstance = targetClass.newInstance();
			copyValue(srcObj,newInstance);
			logger.debug("将对象【"+srcObj+"(来自["+getUrlByClass(srcObj.getClass())+"])"+"】转化成了对象【"+newInstance+"(来自["+getUrlByClass(newInstance.getClass())+"])"+"】不相符，进行了类型转换！");
			return newInstance;
		} catch (Exception e) {
			throw new SystemException("",e);
		}
	}
	
	/**
	 * 拷贝对象信息
	 * @param orig	源对象
	 * @param dest	目标对象
	 */
	public static void copyValue(Object orig, Object dest){
		//判断源对象为空的情况
		if(orig==null){
			return;
		}

		Field[] origFields = orig.getClass().getDeclaredFields();
		Class<? extends Object> destClass = dest.getClass();
		for (Field field : origFields) {
			if(isFinalField(field) || isStaticField(field)){
				continue;
			}
			
			//取得源对象域值
			field.setAccessible(true);
			Object origValue = null;
			try {
				origValue = field.get(orig);
			} catch (Exception e) {
				throw new SystemException("从对象【"+orig+"】中取得域【"+field+"】的值时发生异常",e);
			}
			
			//取得目标对象域信息
			Field destField;
			try {
				destField = destClass.getDeclaredField(field.getName());
			} catch (Exception e1) {
				throw new SystemException("",e1);
			}

			//判断目标对象是否存在对应域
			if(destField==null){
				throw new SystemException("未能找到对象【"+dest+"】的【"+field.getName()+"】域！");
			}
			
			//对目标对象域进行设置值
			destField.setAccessible(true);
			try {
				destField.set(dest, origValue);
			} catch (Exception e) {
				throw new SystemException("向对象【"+dest+"】的【"+destField+"】域赋值【"+origValue+"】时发生异常",e);
			}
		}
	}
	
	/**
	 * 调用指定对象的指定方法
	 * 
	 * @param targerObject
	 *            被调用对象
	 * @param method
	 *            被调用方法
	 * @param args
	 *            调用参数
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object invokeMethod(Object targerObject, Method method, Object... args){
		//验证参数数量
		Class<?>[] parameterTypes = method.getParameterTypes();
		if(parameterTypes.length!=args.length){
			throw new SystemException("方法【"+method+"】的参数数量【"+parameterTypes.length+"】和反射调用时的参数数量【"+args.length+"】不相符，无法完成调用！");
		}
		
		try {
			return method.invoke(targerObject, args);
		} catch (Exception e) {
			throw new SystemException("调用对象【"+targerObject+"】的方法【"+method+"】，参数为【"+args+"】时发生异常：",e);
		}
	}

	public static void main(String[] args) {
		try {
			boolean.class.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 取得指定对象指定方法的相同方法引用(主要用于获取接口方法在指定对象所属类型的实现方法引用)
	 * @param obj
	 * @param interfaceMethod
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	public static Method getMethod(Object obj,Method interfaceMethod){
		try {
			return obj.getClass().getMethod(interfaceMethod.getName(), interfaceMethod.getParameterTypes());
		} catch (Exception e) {
			throw new SystemException("",e);
		}
	}

	/**
	 * 取得对象的类型信息
	 * 
	 * @param obj
	 * @return
	 */
	static String getObjType(Object obj) {
		if (obj == null) {
			return ClassUtils.CLASS_NAME_NULL;
		}
		return obj.getClass().getName();
	}

	/**
	 * 类型信息:null
	 */
	static final String CLASS_NAME_NULL = "null";
	
	/**
	 * 判断两个类直接的父子关系
	 * @param subClass
	 * @param supClass
	 * @return
	 */
	public static boolean asSubclass(Class<?> subClass,Class<?> supClass){
		try {
			subClass.asSubclass(supClass);
			return true;
		} catch (Throwable e) {
			return false;
		}
	}
}