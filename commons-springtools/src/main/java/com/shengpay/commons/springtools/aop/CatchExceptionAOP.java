package com.shengpay.commons.springtools.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 发生异常时,捕获后正常返回null)
 * @description	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDOCorporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: CatchExceptionAOP.java,v 1.0 2010-6-7 下午09:32:09 lindc Exp $
 * @create		2010-6-7 下午09:32:09
 */
public class CatchExceptionAOP implements MethodInterceptor {

	/*
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	public Object invoke(MethodInvocation mi) throws Throwable {
		try {
			return mi.proceed();
		} catch (Throwable e) {
			return null;
		}
	}


}
