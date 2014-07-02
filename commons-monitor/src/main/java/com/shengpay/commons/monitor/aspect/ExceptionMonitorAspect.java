package com.shengpay.commons.monitor.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import com.shengpay.commons.monitor.handler.ExceptionHandlerService;
import com.shengpay.commons.monitor.sdomonitor.SdoExceptionMonitor;

/**
 * @title 异常监控Aspect
 * @description	
 * @usage		
 * @copyright	Copyright 2011 SDO Corporation. All rights reserved.
 * @company		SDO Corporation.
 * @author		wangjinhua <wangjinhua@snda.com>
 * @version		$Id: ExceptionMonitorAspect.java, v 1.0 Apr 20, 2011 $
 * @create		Apr 20, 2011 3:43:49 PM
 */

@Aspect
public class ExceptionMonitorAspect {
	
	@Autowired
	private ExceptionHandlerService exceptionHandlerService;
	
	private static final Logger logger = Logger.getLogger(ExceptionMonitorAspect.class);
	
	@AfterThrowing(pointcut = "within(com.shengpay..*) && execution(* *..*Service*.*(..))", throwing = "e")
	public void exceptionMonitor(JoinPoint joinPoint, Throwable e) {
		if (joinPoint.getTarget() instanceof ExceptionHandlerService || joinPoint.getTarget() instanceof SdoExceptionMonitor) {
			logger.error("An exception has been thrown from monitor internal " + joinPoint.getTarget().getClass(), e);
			return;
		}
		exceptionHandlerService.handleException(e);
	}
	
}
