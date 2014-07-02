/**
 * 
 */
package com.shengpay.commons.springtools.util;

import java.lang.reflect.Method;
import java.util.List;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

import com.shengpay.commons.core.base.LogOutAnn;
import com.shengpay.commons.core.threadlocal.ThreadLocalUtils;
import com.shengpay.commons.core.utils.ClassUtils;
import com.shengpay.commons.core.utils.ThrowableUtils;
import com.shengpay.commons.springtools.methodcallinfo.MethodCallInfo;

/**
 * @author lindongcheng
 *
 */
public class AopClassUtils {
	
	/**
	 * 系统日志输出句柄
	 */
	public static Logger	logger	= Logger.getLogger(AopClassUtils.class);
	
	/**
	 * 获取调用信息;
	 * 
	 * @param mi
	 * @return
	 */
	public static String getMethodCallInfo(MethodInvocation mi) {
		return ClassUtils.getMethodCallInfo(mi.getThis(), mi.getMethod(), mi.getArguments());
	}
	
	/**
	 * 取得方法名称(例:ClassUtils.getMethodName)
	 * 
	 * @param mi
	 * @return
	 */
	public static String getMethodSimpleName(MethodInvocation mi) {
		Method method = mi.getMethod();
		return ClassUtils.getMethodSimpleName(mi.getThis(), method);
	}
	
	/**
	 * 取得一个方法的完整名称
	 * 
	 * @param mi
	 * @return
	 */
	public static String getMethodFullName(MethodInvocation mi) {
		Method method = mi.getMethod();
		return ClassUtils.getMethodFullName(method);
	}
	
	/**
	 * @param mi
	 * @param result
	 * @return
	 */
	public static String getReturnInfo(MethodInvocation mi, Object result) {
		return mi.getMethod().getReturnType().equals(void.class) ? "void" : String.valueOf(result);
	}
	
	/**
	 * @param mi
	 * @return
	 * @throws Throwable
	 */
	public static Object proceedAndLogOutMethod(MethodInvocation mi) throws Throwable {
		return proceedAndLogOutMethod(mi, false, false);
	}
	
	public static Object proceedAndLogOutMethod(MethodInvocation mi, boolean printMethodExpentStack, boolean printMethodExpentTops) throws Throwable {
		LogOutAnn annotation = mi.getMethod().getAnnotation(LogOutAnn.class);
		if(annotation!=null && annotation.disableLogOut()) {
			return mi.proceed();
		}
		
		//整理调用方法的信息;
		String methodId = "【callId:" + System.currentTimeMillis() + "-" + RandomStringUtils.randomNumeric(4) + "】";
		String userInfo = ThreadLocalUtils.getUserId() != null ? "【userId:" + ThreadLocalUtils.getUserLoginName() + "-" + ThreadLocalUtils.getUserId() + "】" : "";
		String ipInfo = ThreadLocalUtils.getClientIp() != null ? "【ip:" + ThreadLocalUtils.getClientIp() + "】" : "";
		String threadInfo = "【threadId:" + Thread.currentThread().getId() + "】";
		String methodFullName = "【 " + getMethodCallInfo(mi) + "】";
		String methodSimpleName = "【 " + getMethodSimpleName(mi) + "()】";
		String methodFullInfo = threadInfo + userInfo + ipInfo + methodId + methodFullName;
		String methodSimpleInfo = threadInfo + userInfo + ipInfo + methodId + methodSimpleName;
		
		//在调用方法前后进行日志输出
		logger.info(methodFullInfo);
		MethodCallInfo mci = new MethodCallInfo(mi,methodFullInfo);
		try {
			Object result = mci.proceed();
			String returnInfo = getReturnInfo(mi, result);
			logger.info(methodSimpleInfo + "  return 【" + returnInfo + "】【用时:" + mci.getMethodExpendMillInfo() + "毫秒】");
			return result;
		} catch (Throwable t) {
			String throwableInfo = ThrowableUtils.getThrowableInfo(t);
			logger.error(methodSimpleInfo + "  throw 【" + throwableInfo + "】【用时:" + mci.getMethodExpendMillInfo() + "毫秒】");
			throw t;
		}finally {
			printInfo(mci,printMethodExpentStack,printMethodExpentTops);
		}
	}

	/**
	 * @param mci
	 * @param printMethodExpentStack
	 * @param printMethodExpentTops
	 */
	private static void printInfo(MethodCallInfo mci, boolean printMethodExpentStack, boolean printMethodExpentTops) {
		if(printMethodExpentStack) {
			mci.printMethodExpentStack(System.out);
		}
		
		if(printMethodExpentTops) {
			mci.printMethodExpentTops(System.out);
		}
		
	}
	
}
