/**
 * 
 */
package com.shengpay.commons.springtools.proxy.facade;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.shengpay.commons.core.exception.SystemException;

/**
 * facade模式代理工厂
 * 
 * @author lindongcheng
 * 
 */
public class FacadeProxyFactory implements FactoryBean<Object>, InvocationHandler, InitializingBean {

	/**
	 * 被代理的对象的列表
	 */
	private List<Object> targets;

	/**
	 * 对外暴露的接口
	 */
	private Class<?> proxyInterface;

	/**
	 * 被调用方法到所属对象的映射
	 */
	private Map<Method, Object> method2TargetMap;

	@Override
	public Object getObject() throws Exception {
		return Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[] { proxyInterface }, this);
	}

	@Override
	public Class<?> getObjectType() {
		return proxyInterface;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object targetObject = method2TargetMap.get(method);
		if (targetObject == null) {
			throw new SystemException("未找到方法【" + method + "】的实现对象");
		}
		return method.invoke(targetObject, args);
	}

	/**
	 * 初始化操作：取得所有被代理对象的方法映射
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		method2TargetMap = new HashMap<Method, Object>();
		for (Object target : targets) {
			Type[] genericInterfaces = target.getClass().getGenericInterfaces();
			for (Type type : genericInterfaces) {
				Method[] declaredMethods = ((Class<?>)type).getDeclaredMethods();
				for (Method method : declaredMethods) {
					method2TargetMap.put(method, target);
				}
			}
		}
	}

	public void setTargets(List<Object> targets) {
		this.targets = targets;
	}

	public void setProxyInterface(Class<?> proxyInterface) {
		this.proxyInterface = proxyInterface;
	}

}
