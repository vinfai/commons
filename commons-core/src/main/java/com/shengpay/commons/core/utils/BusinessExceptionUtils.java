/**
 * 
 */
package com.shengpay.commons.core.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.shengpay.commons.core.exception.BusinessException;
import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.propertiesfile.PropertiesFileHandler;
import com.shengpay.commons.core.propertiesfile.PropertiesFileHandlerImplForResourceBundle;

/**
 * 业务异常工具类
 * @description 用以从资源文件获取业务信息
 * @author Lincoln
 */

public class BusinessExceptionUtils {
	
	/**
	 * 系统日志输出句柄
	 */
	private static Logger logger = Logger.getLogger(BusinessExceptionUtils.class);

	private static Set<String> bcFilePathArr;

	private static List<PropertiesFileHandler> messageResourceHandlerList;
	static {
		//取得保护业务代码文件的路径信息
		String fileType=".properties";
		messageResourceHandlerList = new ArrayList<PropertiesFileHandler>();
		Set<String> allPropertiesFilePathArr = ClassUtils.getClassSetByPackageName("META-INF",fileType );
		bcFilePathArr=new HashSet<String>();
		for (String bcFile : allPropertiesFilePathArr) {
			if(bcFile.indexOf("bc_")==-1){
				continue;
			}
			
			try {
				messageResourceHandlerList.add(new PropertiesFileHandlerImplForResourceBundle(bcFile.replace(fileType, "")));
			} catch (Throwable t) {
				logger.error("绑定资源文件[" + bcFile + "]时发生错误[" + t.getMessage() + "],现已跳过该文件!");
			}
			bcFilePathArr.add(bcFile);
		}
	}

	/**
	 * 获取业务异常信息
	 * 
	 * @param e
	 * @return
	 */
	public static String getBusinessInfo(BusinessException e) {
		return getBusinessInfo(e.getBusinessCode(), e.getArgs());
	}

	/**
	 * 获取业务异常信息
	 * 
	 * @param businessCode 业务信息代码
	 * @param businessArgs 业务信息参数
	 * @return
	 */
	public static String getBusinessInfo(String businessCode, Object ... businessArgs) {
		for (PropertiesFileHandler messageResourceHandler : messageResourceHandlerList) {
			String message = messageResourceHandler.getStringCanNull(businessCode, businessArgs);
			if (message != null) {
				return message;
			}
		}

		throw new SystemException("未能从资源[" + StringUtils.toString(bcFilePathArr) + "]中找到业务异常[" + businessCode + "]对应的业务信息");
	}
	
	
	/**
	 * 读取错误信息(dwr调用)
	 * 
	 * @param servletContext
	 * @param errorMessageException
	 * @return
	 */
	public static String getErrorInfoByDWR(String businessCode, String[] businessArgs) {
		if (null == businessCode || null == businessArgs)
			return null;
		for (PropertiesFileHandler messageResourceHandler : messageResourceHandlerList) {
			String message = messageResourceHandler.getStringCanNull(businessCode, businessArgs);
			if (message != null) {
				return message;
			}
		}
		throw new SystemException("未能从资源[" + StringUtils.toString(bcFilePathArr) + "]中找到业务异常[" + businessCode + "]对应的业务信息");
	}
	

	public static void main(String[] args) {
		System.out.println(new BusinessExceptionUtils().getBusinessInfo(new BusinessException("bc.commons.lincoln.1", 1)));
	}

}
