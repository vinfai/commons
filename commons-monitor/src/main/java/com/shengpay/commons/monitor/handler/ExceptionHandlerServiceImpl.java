package com.shengpay.commons.monitor.handler;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.sdo.transbutton.common.email.EmailSender;
import com.sdo.transbutton.common.sms.SmsSender;
import com.shengpay.commons.core.exception.BusinessException;
import com.shengpay.commons.core.exception.BusinessException.Level;
import com.shengpay.commons.core.exception.CheckException;
import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.utils.BusinessExceptionUtils;
import com.shengpay.commons.core.utils.DateTimeUtils;
import com.shengpay.commons.core.utils.ThrowableUtils;
import com.shengpay.commons.monitor.sdomonitor.SdoExceptionMonitor;
import com.shengpay.commons.monitor.utils.IPUtils;

/**
 * @title ExceptionHandlerServiceImpl
 * @description	
 * @usage		
 * @copyright	Copyright 2011 SDO Corporation. All rights reserved.
 * @company		SDO Corporation.
 * @author		wangjinhua <wangjinhua@snda.com>
 * @version		$Id: ExceptionHandlerServiceImpl.java, v 1.0 Apr 21, 2011 $
 * @create		Apr 21, 2011 1:30:24 PM
 */

public class ExceptionHandlerServiceImpl implements ExceptionHandlerService {
	
	/*
	 * 保存近期异常的列表(该列表定长,超出部分将被删除)
	 */
	private List<Throwable> exceptionList;
	
	private String[] notifyMpArr;

	private String fromEmail;

	private String[] toEmails;
	
	private int smsMaxLength;
	
	private String ipAddress;
	
	private String moduleName;

	private boolean monitorSwitch=true;
	
	private boolean sendSmsSwitch=true;
	
	private boolean sendEmailSwitch=true;
	
	private boolean sdoMonitorSwitch=true;

	@Autowired
	@Qualifier("sdoExceptionMonitor_monitor")
	private SdoExceptionMonitor sdoMonitor;
	
	@Autowired
	@Qualifier("smsSender_monitor")
	private SmsSender smsSender;
	
	@Autowired
	@Qualifier("emailSender_monitor")
	private EmailSender emailSender;
	
	// init ip address
	{
		try {
			ipAddress = IPUtils.getServerIPsStr();
		} catch (Exception e) {
			throw new SystemException("获取ip地址出错", e);
		}
	}
	
	
	private static final Logger logger = Logger.getLogger(ExceptionHandlerServiceImpl.class);
	
	@Override
	public void handleException(Throwable e) {
		try {
			if (!monitorSwitch) {
				return ;
			}
			if (exceptionList.contains(e)) {
				return ;
			}
			
			exceptionList.add(e);
			if (e instanceof SystemException) {
				onSystemException((SystemException) e);
			} else if (e instanceof CheckException) {
				onCheckException((CheckException) e);
			} else if (e instanceof BusinessException) {
				onBusinessException((BusinessException) e);
			} else {
				onOtherException(e);
			}
		} catch (Exception e2) {
			logger.error("AOP异常处理异常", e2);
		}
	}
	
	private void onSystemException(SystemException e) {
		logger.error("A SystemException has been thrown", e);
		sendEmail(e);
		sendSms(e);
		sendReportToSDOMonitor(SystemException.class, e);
	}

	private void onCheckException(CheckException e) {
		logger.error("A CheckException has been thrown", e);
		sendSms(e);
		sendEmail(e);
		sendReportToSDOMonitor(CheckException.class, e);
	}

	private void onBusinessException(BusinessException e) {
		if (!Level.NORMAL.equals(e.getLevel())) {
			logger.info("A BusinessException has been thrown", e);
		}
		if (Level.WARN.equals(e.getLevel())) {
			sendEmail(e);
		} else if (Level.ERROR.equals(e.getLevel()) || Level.FATAL.equals(e.getLevel())) {
			sendEmail(e);
			sendSms(e);
			sendReportToSDOMonitor(BusinessException.class, e);
		}

	}

	private void onOtherException(Throwable e) {
		logger.error("An unknown Exception has been thrown", e);
		sendEmail(e);
		sendSms(e);
		sendReportToSDOMonitor(e.getClass(), e);
	}
	
	private void sendReportToSDOMonitor(Class<? extends Throwable> exceptionClazz, Throwable e) {
		if (sdoMonitorSwitch) {
			try {
				sdoMonitor.reportToMonitor(moduleName, e);
			} catch (Exception e2) {
				logger.error("Exception happened when report " + exceptionClazz + " to sdo monitor", e2);
			}
		}
	}
	
	private void sendSms(Throwable e) {
		if (sendSmsSwitch) {
			if (ArrayUtils.isEmpty(notifyMpArr)) {
				logger.warn("没有手机地址，不发送异常");
				return;
			}

			//整理短信内容
			String smsMsg = moduleName + "【" + ThrowableUtils.getRootMessage(e) + "】";
			if (smsMsg.length() > smsMaxLength) {
				smsMsg = smsMsg.substring(0, smsMaxLength);
			}

			//一一发送短信
			try {
				smsSender.sendSms(notifyMpArr, smsMsg);
			} catch (Exception e1) {
				logger.error("发送异常短信异常", e1);
			}
		}
	}

	private void sendEmail(Throwable e) {
		if (sendEmailSwitch) {
			if (ArrayUtils.isEmpty(toEmails)) {
				logger.warn("没有发送邮件地址，不发送异常");
				return ;
			}
			String businessInfo = StringUtils.EMPTY;
			if (e instanceof BusinessException) {
				businessInfo = BusinessExceptionUtils.getBusinessInfo((BusinessException) e);
			}
			String bugNum = DateTimeUtils.getCurrentExacDateTime();
			String mailSubject = moduleName + " 系统异常【" + ipAddress + "】【" + bugNum + "】【" + ThrowableUtils.getRootMessage(e) + "】";
			String mailBody = "<pre>" + businessInfo + "\n" + ThrowableUtils.getThrowableInfo(e)
					+ "</pre>";
			
			// emailSender.sendEmail(fromEmail, toEmails, mailBody, null, null, mailSubject, null, null);
			try {
				emailSender.sendEmail(fromEmail, toEmails, mailSubject, mailBody);
			} catch (Exception e1) {
				logger.error("发送异常邮件异常", e1);
			}
		}
	}
	
	public void setSendSmsSwitch(boolean sendSmsSwitch) {
		this.sendSmsSwitch = sendSmsSwitch;
	}
	
	public void setSendEmailSwitch(boolean sendEmailSwitch) {
		this.sendEmailSwitch = sendEmailSwitch;
	}
	
	public void setSdoMonitorSwitch(boolean sdoMonitorSwitch) {
		this.sdoMonitorSwitch = sdoMonitorSwitch;
	}

	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}

	public void setEmailSender(EmailSender emailSender) {
		this.emailSender = emailSender;
	}

	public void setNotifyMpArr(String[] notifyMpArr) {
		this.notifyMpArr = notifyMpArr;
	}

	public void setExceptionList(List<Throwable> exceptionList) {
		this.exceptionList = exceptionList;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public void setToEmails(String[] toEmails) {
		this.toEmails = toEmails;
	}

	public void setSmsMaxLength(int smsMaxLength) {
		this.smsMaxLength = smsMaxLength;
	}

	public void setMonitorSwitch(boolean monitorSwitch) {
		this.monitorSwitch = monitorSwitch;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

}
