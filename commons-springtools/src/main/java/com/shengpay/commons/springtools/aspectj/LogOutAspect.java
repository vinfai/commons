package com.shengpay.commons.springtools.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

import com.shengpay.commons.core.utils.ClassUtils;
import com.shengpay.commons.springtools.util.AopClassUtils;

/**
 * @title 日志输出Aspect
 * @description
 * @usage
 * @copyright Copyright 2011 SDO Corporation. All rights reserved.
 * @company SDO Corporation.
 * @author wangjinhua <wangjinhua@snda.com>
 * @version $Id: ExceptionMonitorAspect.java, v 1.0 Apr 20, 2011 $
 * @create Apr 20, 2011 3:43:49 PM
 */

public class LogOutAspect {
	/**
	 * 
	 */
	private boolean	printMethodExpentStack=false;
	
	/**
	 * 
	 */
	private boolean	printMethodExpentTops=false;

	public Object logout(ProceedingJoinPoint mi) throws Throwable {
		ProxyMethodInvocation method=(ProxyMethodInvocation) ClassUtils.getDeclaredFieldValue((MethodInvocationProceedingJoinPoint) mi, "methodInvocation");
		return AopClassUtils.proceedAndLogOutMethod(method,printMethodExpentStack,printMethodExpentTops);
	}

	/**
	 * 获取【{@link #printMethodExpentStack printMethodExpentStack}】
	 * @return 类型：boolean
	 */
	public boolean isPrintMethodExpentStack() {
		return printMethodExpentStack;
	}

	/**
	 * 设置【{@link #printMethodExpentStack printMethodExpentStack}】
	 * @param 类型：boolean
	 */
	public void setPrintMethodExpentStack(boolean printMethodExpentStack) {
		this.printMethodExpentStack = printMethodExpentStack;
	}

	/**
	 * 获取【{@link #printMethodExpentTops printMethodExpentTops}】
	 * @return 类型：boolean
	 */
	public boolean isPrintMethodExpentTops() {
		return printMethodExpentTops;
	}

	/**
	 * 设置【{@link #printMethodExpentTops printMethodExpentTops}】
	 * @param 类型：boolean
	 */
	public void setPrintMethodExpentTops(boolean printMethodExpentTops) {
		this.printMethodExpentTops = printMethodExpentTops;
	}

}
