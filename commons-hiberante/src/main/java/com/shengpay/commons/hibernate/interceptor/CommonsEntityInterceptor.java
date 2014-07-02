package com.shengpay.commons.hibernate.interceptor;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import com.shengpay.commons.core.base.UpdateTimeAtn;


public class CommonsEntityInterceptor extends EmptyInterceptor{

	/**
	 * 类对应更新时间域列表映射（缓存）
	 */
	private Map<Class<?>,List<String>> class2UpdateTimeFieldNamesMap=new HashMap<Class<?>, List<String>>();
	
	/**
	 * 数据更新入库时，将其更新时间域设置最新时间
	 * @see org.hibernate.EmptyInterceptor#onFlushDirty(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
	 */
	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
		if(entity==null) {
			return false;
		}
		
		List<String> updateTimeFieldNames = getUpdateTimeFieldNamesByCache(entity.getClass());
		if(updateTimeFieldNames.isEmpty()) {
			return false;
		}
		
		for (int i = 0; i < propertyNames.length; i++) {
			if(updateTimeFieldNames.contains(propertyNames[i])) {
				currentState[i]=new Date();
			}
		}
		return true;
	}

	/**
	 * 从缓存里获取指定类型更新时间属性列表
	 * @param entity
	 * @return
	 */
	private List<String> getUpdateTimeFieldNamesByCache(final Class<?> aClass) {
		List<String> list = class2UpdateTimeFieldNamesMap.get(aClass);
		if(list!=null) {
			return list;
		}
		
		List<String> updateTimeFieldNames = getUpdateTimeFieldNames(aClass);
		
		class2UpdateTimeFieldNamesMap.put(aClass, updateTimeFieldNames);
		return updateTimeFieldNames;
	}

	/**
	 * 获取指定类型更新时间属性列表
	 * @param aClass
	 * @return
	 */
	private List<String> getUpdateTimeFieldNames(final Class<?> aClass) {
		List<String> updateTimeFieldNames=new ArrayList<String>();
		Class<? extends Object> class1 = aClass;
		while(class1!=null) {
			Field[] declaredFields = class1.getDeclaredFields();
			for (Field field : declaredFields) {
				UpdateTimeAtn updateTimeAtn = field.getAnnotation(UpdateTimeAtn.class);
				if(updateTimeAtn!=null) {
					updateTimeFieldNames.add(field.getName());
				}
			}
			class1=class1.getSuperclass();
		}
		return updateTimeFieldNames;
	}
}
