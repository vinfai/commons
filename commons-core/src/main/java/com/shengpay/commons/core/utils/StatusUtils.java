/**
 * 
 */
package com.shengpay.commons.core.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.shengpay.commons.core.base.StatusAtn;
import com.shengpay.commons.core.exception.SystemException;

/**
 * 状态工具类
 * 
 * @author lindongcheng
 * 
 */
public class StatusUtils {

	/**
	 * 状态值映射
	 */
	private Map<Class<?>, Map<Integer, Object>> statusMap = new HashMap<Class<?>, Map<Integer, Object>>();

	/**
	 * 状态值映射
	 */
	private Map<String, Map<Integer, String>> nameMap = new HashMap<String, Map<Integer, String>>();

	/**
	 * 单例
	 */
	private static final StatusUtils instance = new StatusUtils();
	
	private Logger logger=Logger.getLogger(StatusUtils.class);

	/**
	 * 获取单例
	 * 
	 * @return
	 */
	public static StatusUtils getInstance() {
		return instance;
	}
	
	public Map<Integer, String> getNameMap(String statusClassSimpleName){
		return nameMap.get(statusClassSimpleName);
	}

	private StatusUtils() {
		putStatus(ClassUtils.getClassSetByPackageName("com.test", StatusAtn.class));
		putStatus(ClassUtils.getClassSetByPackageName("com.shengpay", StatusAtn.class));
	}

	private void putStatus(Set<Class<?>> classSetByPackageName) {
		for (Class<?> class1 : classSetByPackageName) {
			try {
				putStatus2Map(class1);
			} catch (Exception e) {
				logger.error("",e);
			}
		}
	}

	/**
	 * 将指定类型的状态信息添加到映射
	 * 
	 * @param class1
	 */
	private void putStatus2Map(Class<?> class1) {

		put2StatusMap(class1);
		put2NameMap(class1);

	}

	private void put2NameMap(Class<?> class1) {
		StatusAtn statusAtn = class1.getAnnotation(StatusAtn.class);
		Class<?> statusClass = statusAtn.statusClass();
		String simpleName = statusClass.getSimpleName();
		Map<Integer, String> nMap = nameMap.get(simpleName);
		if (nMap == null) {
			nMap = new HashMap<Integer, String>();
			nameMap.put(simpleName, nMap);
		}
		String existName = nMap.get(statusAtn.value());
		if (StringUtils.notBlank(existName)) {
			logger.warn("状态类型【" + statusClass + "】针对值【" + statusAtn.value() + "】已经有状态名称【" + existName + "】,不应在类型【" + class1 + "】中再重复添加【" + StatusAtn.class + "】注解！");
			return;
		}
		nMap.put(statusAtn.value(), statusAtn.name());
	}

	private void put2StatusMap(Class<?> class1) {
		StatusAtn statusAtn = class1.getAnnotation(StatusAtn.class);
		Class<?> statusClass = statusAtn.statusClass();
		Map<Integer, Object> sMap = statusMap.get(statusClass);
		if (sMap == null) {
			sMap = new HashMap<Integer, Object>();
			statusMap.put(statusClass, sMap);
		}
		int intValue = statusAtn.value();
		Object existStatus = sMap.get(intValue);
		Object inst=ClassUtils.newInstance(class1);
		if (existStatus != null && inst!=null) {
			throw new SystemException("状态类型【" + statusClass + "】针对值【" + intValue + "】已经有状态实例【" + existStatus + "】,不应在类型【" + class1 + "】中再重复添加【" + StatusAtn.class + "】注解！");
		}
		if(inst!=null){
			sMap.put(intValue, inst);
		}
	}

	/**
	 * 获取指定状态类型对应值的状态对象
	 * 
	 * @param statusClass
	 *            状态类型
	 * @param value
	 *            状态值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getStatus(Class<T> statusClass, int value) {
		Map<Integer, Object> map = statusMap.get(statusClass);
		if (map == null) {
			throw new SystemException("未找到状态类型【" + statusClass + "】的任何注解信息，请确认相关类已添加【" + StatusAtn.class + "】注解！");
		}

		Object status = map.get(value);
		if (status == null) {
			throw new SystemException("从状态类型【" + statusClass + "】的所有注解中，未找到值为【" + value + "】的配置信息，请确认注解编写是否正确");
		}
		return (T) status;
	}
	
	/**
	 * 获取状态对象对应的值
	 * 
	 * @param status
	 * @return
	 */
	public Integer[] getValues(Object[] status) {
		Integer[] values=new Integer[status.length];
		for (int i = 0; i < status.length; i++) {
			values[i]=getValue(status[i]);
		}
		
		return values;
	}

	/**
	 * 获取状态对象对应的值
	 * 
	 * @param status
	 * @return
	 */
	public int getValue(Object status) {
		if (status == null) {
			throw new SystemException("获取状态对应值时状态对象不能为空！");
		}

		Class<? extends Object> class1 = status.getClass();
		StatusAtn statusAtn = class1.getAnnotation(StatusAtn.class);
		if (statusAtn == null) {
			throw new SystemException("类型【" + class1 + "】无【" + StatusAtn.class + "】注解，无法获取其对应的状态值!");
		}

		return statusAtn.value();
	}
	
	/**
	 * 获取类型名称
	 * @param statusClass
	 * @param value
	 * @return
	 */
	public String getName(Class<?> statusClass, int value) {
		Map<Integer, String> map = nameMap.get(statusClass.getSimpleName());
		if(map==null) {
			throw new SystemException("未找到类型【"+statusClass+"】对应的常量信息！");
		}
		return map.get(value);
	}

	public Object[] getStatus(Class<?> class1, Integer[] statusIntArr) {
		if(statusIntArr==null || statusIntArr.length==0) {
			return new Object[0];
		}
		
		Object[] statusArr=new Object[statusIntArr.length];
		for (int i = 0; i < statusIntArr.length; i++) {
			statusArr[i]=getStatus(class1, statusIntArr[i]);
		}
		return statusArr;
	}

}
