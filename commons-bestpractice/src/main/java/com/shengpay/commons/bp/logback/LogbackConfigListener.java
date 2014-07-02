/*
 * Copyright 2012 shengpay.com, Inc. All rights reserved.
 * shengpay.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : kuguobing
 * create time : 2012-11-26 上午11:08:27
 */
package com.shengpay.commons.bp.logback;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 
 * 功能描述：@see {@link Log4jConfigListener}
 * @author kuguobing
 * time : 2012-11-26 上午11:08:38
 */
public class LogbackConfigListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
        LogbackWebConfigurer.initLogging(event.getServletContext());
    }

    public void contextDestroyed(ServletContextEvent event) {
        LogbackWebConfigurer.shutdownLogging(event.getServletContext());
    }
}
