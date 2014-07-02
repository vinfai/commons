package com.shengpay.commons.hibernate.propertiescopy;

import static com.shengpay.commons.core.utils.ClassUtils.getArrayOrigClass;
import static com.shengpay.commons.core.utils.ClassUtils.invokeMethod;
import static com.shengpay.commons.core.utils.ClassUtils.isNull;
import static com.shengpay.commons.core.utils.ClassUtils.newAInstance;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.utils.ClassUtils;
import com.shengpay.commons.hibernate.queryservice.BaseDTO;

public class PropertiesCopy {
	private final Object orig;
	private final Object dest;
	private final Object copyTemplate;
	private final boolean allConvertCopy;

	private Method method;
	private String methodName;
	private Class<?> propertyType;

	public PropertiesCopy(Object orig, Object dest) {
		this(orig, dest, dest);
	}
	
	public PropertiesCopy(Object orig, Object dest, Object queryDTO) {
		if (orig == null || dest == null || queryDTO==null) {
			throw new SystemException("参数不能为空！");
		}
		
		this.orig = orig;
		this.dest = dest;
		this.copyTemplate = queryDTO;
		this.allConvertCopy = isAllConvertCopy(queryDTO);
	}

	public Object copyProperties() {
		Method[] destMethods = dest.getClass().getMethods();
		for (Method destMethod : destMethods) {
			copyByMethod(destMethod);
		}

		return dest;
	}
	
	public void copyByMethod(Method destMethod) {
		setMethod(destMethod);

		if (!isSetterMethod()) {
			return;
		}

		Method getterMethod = getGetterMethod(orig);
		if (getterMethod == null) {
			return;
		}

		Object propertyValue = invokeMethod(orig, getterMethod);
		if (isNull(propertyValue)) {
			return;
		}

		setProperty(propertyValue);
	}

	private void setMethod(Method destMethod) {
		method = destMethod;
		methodName = destMethod.getName();
		propertyType = getPropertyType();
	}

	private void setProperty(Object propertyValue) {
		if (propertyType.isAssignableFrom(propertyValue.getClass())) {
			invokeMethod(dest, method, propertyValue);
			return;
		}

		if (!isConvertCopy()) {
			return;
		}

		invokeMethod(dest, method, convertValue(propertyValue));
	}

	@SuppressWarnings({ "rawtypes" })
	private Object convertValue(Object propertyValue) {
		Object template = getPropertyCopyTemplate();
		if (!propertyType.isArray()) {
			return copyByTemplate(propertyValue, template);
		}
		
		//转换数组
		List list = copyListByTemplate((List) propertyValue, template);
		return list2array(list);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object list2array(List list) {
		try {
			Object newInstance = Array.newInstance(getArrayOrigClass(propertyType), 0);
			return list != null ? list.toArray((Object[]) newInstance) : null;
		} catch (Exception e) {
			throw new SystemException("", e);
		}
	}


	public boolean isSetterMethod() {
		return methodName.startsWith("set") && method.getParameterTypes().length == 1 && Modifier.isPublic(method.getModifiers()) && !Modifier.isStatic(method.getModifiers());
	}

	public Method getGetterMethod(Object obj) {
		try {
			return obj.getClass().getMethod(getGetterMethodName());
		} catch (Exception e) {
			return null;
		}
	}

	private String getGetterMethodName() {
		if(propertyType.equals(boolean.class)) {
			return "is" + methodName.substring(3);
		}else {
			return "get" + methodName.substring(3);
		}
		
	}

	private Class<?> getPropertyType() {
		if (!isSetterMethod()) {
			return null;
		}

		Class<?>[] parameterTypes = method.getParameterTypes();
		if (parameterTypes == null || parameterTypes.length != 1) {
			throw new SystemException("方法【" + method + "】有多个参数，不是属性设置器！");
		}

		return parameterTypes[0];
	}

	public Object getPropertyTypeInstance() {
		try {
			if (propertyType.isArray()) {
				return ClassUtils.getArrayOrigClass(propertyType).newInstance();
			} else {
				return propertyType.newInstance();
			}
		} catch (Exception e) {
			throw new SystemException("", e);
		}
	}

	private boolean isAllConvertCopy(Object queryDTO) {
		if (!(queryDTO instanceof BaseDTO)) {
			return false;
		}
		
		Boolean loadAllDTO = ((BaseDTO) queryDTO).getLoadAllDTO();
		return loadAllDTO == null ? false : loadAllDTO;
	}

	public boolean isConvertCopy() {
		if (allConvertCopy) {
			return true;
		}

		Method getterMethod = getGetterMethod(copyTemplate);
		if (getterMethod == null) {
			return false;
		}
		return !isNull(invokeMethod(copyTemplate, getterMethod));
	}

	public Object getPropertyCopyTemplate() {
		Method getterMethod = getGetterMethod(copyTemplate);
		if (getterMethod == null) {
			throw new SystemException("对象【"+copyTemplate+"】没有【"+method+"】的获取方法，有问题！");
		}

		Object queryDtoPropertyValue = invokeMethod(copyTemplate, getterMethod);
		if (queryDtoPropertyValue == null) {
			return getPropertyTypeInstance();
		}

		if (!queryDtoPropertyValue.getClass().isArray()) {
			return queryDtoPropertyValue;
		}

		if (Array.getLength(queryDtoPropertyValue) > 0) {
			return Array.get(queryDtoPropertyValue, 0);
		}

		try {
			return newAInstance(getArrayOrigClass(queryDtoPropertyValue.getClass()));
		} catch (Exception e) {
			throw new SystemException("", e);
		}

	}

	public static void copy(Object orig, Object dest) {
		new PropertiesCopy(orig,dest).copyProperties();
	}
	
	public static <DestType> List<DestType> copyListByTemplate(List<?> origList, DestType template) {
		List<DestType> list = new ArrayList<DestType>();
		for (Object activePoi : origList) {
			list.add(copyByTemplate(activePoi, template));
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public static <DestType> DestType copyByTemplate(Object orig, DestType template) {
		Object dest = getDestInstance(template);
		try {
			return (DestType) new PropertiesCopy(orig, dest, template).copyProperties();
		} catch (Exception e) {
			throw new SystemException("拷贝对象【" + orig + "】到【" + dest + "】时发生异常：", e);
		}
	}

	private static Object getDestInstance(Object dest) {
		try {
			return ClassUtils.newAInstance(dest.getClass());
		} catch (Throwable e) {
			throw new SystemException("", e);
		}
	}
}
