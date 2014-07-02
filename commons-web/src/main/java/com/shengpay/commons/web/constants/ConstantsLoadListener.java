/**
 * 
 */
package com.shengpay.commons.web.constants;

import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import com.shengpay.commons.core.constants.ConstantsTO;
import com.shengpay.commons.core.constants.ConstantsUtil;
import com.shengpay.commons.web.base.BaseServletContextListener;

/**
 * 常量加载到ServletContext中的监听器(使常量可以在页面上以EL的方式使用)
 * @description	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDO Corporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: ConstantsLoadListener.java,v 1.0 2010-10-8 下午03:03:52 lindongcheng Exp $
 * @create		2010-10-8 下午03:03:52
 */
public class ConstantsLoadListener extends BaseServletContextListener {

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContentEvent) {
        ServletContext servletContext = servletContentEvent.getServletContext();
        
        Collection<ConstantsTO> allConstantsTOList = ConstantsUtil.getAllConstantsTOList();
        for (ConstantsTO ct : allConstantsTOList) {
            servletContext.setAttribute(ct.getFieldName(), ct.getFieldValue());
        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContentEvent) {
        // TODO Auto-generated method stub

    }

}
