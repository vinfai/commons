package com.shengpay.commons.springtools.aop;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ReflectiveMethodInvocation;

import com.shengpay.commons.core.base.CurrUserCheckAnn;
import com.shengpay.commons.core.constants.ConstantsStatusAccount;
import com.shengpay.commons.core.constants.ConstantsTypeUser;
import com.shengpay.commons.core.exception.BusinessException;
import com.shengpay.commons.core.threadlocal.ThreadLocalUtils;

/**
 * 检查登陆用户信息拦截器
 * @description 检查当前线程内是否用用户信息,并检查其类型/状态是否合法
 * @author Lincoln
 */
public class CheckLoginUserAOP implements MethodInterceptor {

	/* (non-Javadoc)
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	public Object invoke(MethodInvocation mi) throws Throwable {
		// 若被调用方法未被[CurrUserCheckAnn]修饰,则不作任何处理,直接返回
		ReflectiveMethodInvocation inv = (ReflectiveMethodInvocation) mi;
		MethodInvocation anotherInvocation = inv.invocableClone();
		Method method = anotherInvocation.getMethod();
		Class<?> declaringClass = method.getDeclaringClass();
		CurrUserCheckAnn classCheck = declaringClass.getAnnotation(CurrUserCheckAnn.class);
		CurrUserCheckAnn methodCheck = method.getAnnotation(CurrUserCheckAnn.class);
		if (classCheck==null && methodCheck == null) {
			return mi.proceed();
		}

		// 判断用户是否已登录
		if ((classCheck!=null && classCheck.isLogined()) || (methodCheck!=null && methodCheck.isLogined())) {
			isLogined();
		}

		// 验证用户是否激活
		if ((classCheck!=null && classCheck.isActive()) || (methodCheck!=null && methodCheck.isActive())) {
			isActive();
		}
		
		// 验证用户是否为管理员
		if ((classCheck!=null && classCheck.isAdmin()) || (methodCheck!=null && methodCheck.isAdmin())) {
			validateUserBtype(ConstantsTypeUser.UB_TYPE_ADMIN);
		}
		
		// 验证用户是否为普通用户
		if ((classCheck!=null && classCheck.isClient()) || (methodCheck!=null && methodCheck.isClient())) {
			validateUserBtype(ConstantsTypeUser.UB_TYPE_CLIENT);
		}

		// 各项检查都通过时,执行目标方法
		return mi.proceed();
	}

	/**
	 * 验证用户类型
	 * @param userType 期望的用户类型
	 * @throws BusinessException 
	 */
	private void validateUserBtype(String userType) throws BusinessException {
		if(isLogined()){
			if(!userType.equals(ThreadLocalUtils.getUserVO().getUserType())){
				throw new BusinessException("bc.commons.lindc.1");
			}
		}
	}

	/**
	 * 验证用户是否为激活状态,否则将抛出异常
	 * @throws BusinessException
	 */
	private void isActive() throws BusinessException {
		if(isLogined()){
			if(!ConstantsStatusAccount.ACCOUNT_STATUS_ACTIVE.equals(ThreadLocalUtils.getUserVO().getUserStatus())){
				throw new BusinessException("bc.commons.zhangxc.3");
			}
		}
	}

	/**
	 * 检查用户是否登录
	 * @return 若用户已登录则返回true
	 * @throws BusinessException 若用户未登录则抛出业务异常,
	 */
	private boolean isLogined() throws BusinessException {
		if ((ThreadLocalUtils.getUserVO() == null)) {
			throw new BusinessException("bc.commons.zhangxc.1");
		}
		
		return true;
	}

}
