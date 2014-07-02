package com.shengpay.commons.core.integralitycheck;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import com.shengpay.commons.core.exception.BusinessException;
import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.utils.ClassUtils;
import com.shengpay.commons.core.utils.StringUtils;

/**
 * 完整性验证
 * @author lindongcheng
 *
 */
public class IntegralityCheck {
	
	/**
	 * 被验证的对象
	 */
	private Object obj;
	
	public IntegralityCheck(Object aObj) {
		this.obj=aObj;
	}
	
	/**
	 * 对象完整性检查，在持久化前或重建后被调用，可以通过抛出BusinessException的方式预警
	 */
	public void doCheck() {
		if(obj==null) {
			return;
		}
		
		Class<?> aClass = obj.getClass();
		while(aClass!=null) {
			integralityCheck(aClass);
			aClass=aClass.getSuperclass();
		}
	}
	
	/**
	 * (递归方法）对指定类型进行完整性验证
	 * @param aClass
	 */
	private void integralityCheck(Class<?> aClass) {
		checkField(aClass);
		checkMethod(aClass);
	}

	private void checkField(Class<?> aClass) {
		Field[] fields = aClass.getDeclaredFields();
		for (Field field : fields) {
			//排除静态属性
			if(ClassUtils.isConstantsField(field)) {
				continue;
			}
			
			//排除无注解属性
			IntegralityCheckAtn annotation = field.getAnnotation(IntegralityCheckAtn.class);
			if(annotation==null) {
				continue;
			}
			
			//检查属性值是否为空
			checkNullField(field, annotation);
		}
	}
	
	private void checkMethod(Class<?> aClass) {
		Method[] methods = aClass.getDeclaredMethods();
		for (Method method : methods) {
			//排除静态属性
			Class<?>[] parameterTypes = method.getParameterTypes();
			if(parameterTypes!=null && parameterTypes.length>0) {
				continue;
			}
			
			//排除无注解属性
			IntegralityCheckAtn annotation = method.getAnnotation(IntegralityCheckAtn.class);
			if(annotation==null) {
				continue;
			}
			
			//检查属性值是否为空
			checkMethodReturnNull(method, annotation);
		}
	}

	/**
	 * 检查属性值是否为空
	 * @param field
	 * @param annotation
	 */
	private void checkNullField(Field field, IntegralityCheckAtn annotation) {
		if(annotation.bcCode4nullValue()==null) {
			return;
		}
		
		if(isNull(field)) {
			String bcCode4nullValue = annotation.bcCode4nullValue();
			if(StringUtils.isBlank(bcCode4nullValue)) {
				throw new SystemException("属性【"+field+"】不能为空！");
			}else {
				throw new BusinessException(bcCode4nullValue,field);
			}
		}
	}
	
	/**
	 * 检查属性值是否为空
	 * @param method
	 * @param annotation
	 */
	private void checkMethodReturnNull(Method method, IntegralityCheckAtn annotation) {
		if(isNull(method)) {
			String bcCode4nullValue = annotation.bcCode4nullValue();
			if(StringUtils.isBlank(bcCode4nullValue)) {
				throw new SystemException("属性【"+method+"】不能为空！");
			}else {
				throw new BusinessException(bcCode4nullValue,method);
			}
		}
	}

	private boolean isNull(Field field) {
		Object fieldValue = ClassUtils.getFieldValue(obj, field);
		if(fieldValue==null) {
			return true;
		}
		
		if(fieldValue instanceof Collection) {
			return ((Collection<?>)fieldValue).isEmpty();
		}
		return false;
	}
	
	private boolean isNull(Method method) {
		Object fieldValue;
		try {
			fieldValue = method.invoke(obj);
		} catch (Exception e) {
			throw new SystemException("调用对象【"+obj+"】的方法【"+method+"】时发生异常：",e);
		}
		if(fieldValue==null) {
			return true;
		}
		
		if(fieldValue instanceof Collection) {
			return ((Collection<?>)fieldValue).isEmpty();
		}
		return false;
	}

}
