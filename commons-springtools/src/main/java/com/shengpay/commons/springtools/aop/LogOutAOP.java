package com.shengpay.commons.springtools.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.shengpay.commons.springtools.util.AopClassUtils;

/**
 * 日志输出拦截器
 * @description
 * @author Lincoln
 */
public class LogOutAOP implements MethodInterceptor {
	/**
	 * 
	 */
	private boolean	printMethodExpentStack=false;
	
	/**
	 * 
	 */
	private boolean	printMethodExpentTops=false;

	/**
	 * 在调用方法前后输出日志
	 * 
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	public Object invoke(MethodInvocation mi) throws Throwable {
		return AopClassUtils.proceedAndLogOutMethod(mi, printMethodExpentStack, printMethodExpentTops);
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
