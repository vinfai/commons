package com.shengpay.commons.core.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import com.shengpay.commons.core.base.BaseObject;
import com.shengpay.commons.core.utils.ClassUtils;

public abstract class LazyLoaderProxy<T> extends BaseObject implements MethodInterceptor {
	protected Object target = null;

	protected boolean isInited = false;

	@Override
	public Object intercept(Object targetProxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		Method proxyMethod = getProxyMethod(method);
		if (proxyMethod != null) {
			return proxyMethod.invoke(this, args);
		}

		if (!isInited) {
			loadTarget();
		}

		if (target == null) {
			return null;
		} else {
			return methodProxy.invoke(target, args);
		}
	}

	private Method getProxyMethod(Method method) throws NoSuchMethodException {
		try {
			Method proxyMethod = getClass().getMethod(method.getName(), method.getParameterTypes());
			proxyMethod.setAccessible(true);
			return proxyMethod;
		} catch (Throwable e) {
			return null;
		}
	}

	synchronized protected void loadTarget() {
		if (!isInited) {
			target = load();
			isInited = true;
		}
	}

	public abstract T load();

	@SuppressWarnings("unchecked")
	public T create() {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(getClassForTarget());
		enhancer.setCallback(this);
		return (T) enhancer.create();
	}

	private Class<?> getClassForTarget() {
		return ClassUtils.getGenericType(getClass(), 0);
	}

	@Override
	public String toString() {
		return isInited ? String.valueOf(target) : "LazyLoaderProxy4" + getClassForTarget().getSimpleName();
	}

	@Override
	public boolean isNull() {
		if (!isInited)
			loadTarget();
		return target == null;
	}
}
