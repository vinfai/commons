package com.shengpay.commons.core.runinfo;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.log4j.Logger;

import com.shengpay.commons.core.email.EmailSender;
import com.shengpay.commons.core.exception.CheckException;
import com.shengpay.commons.core.utils.IOStreamUtil;
import com.shengpay.commons.core.utils.StringUtils;
import com.shengpay.commons.core.utils.ThrowableUtils;

public class RunInfoEmailSenderImpl implements RunInfoEmailSender {
	private static final long MAX_MAIL_BODY_LENGTH = 1024*1024;

	private Logger logger=Logger.getLogger(RunInfoEmailSenderImpl.class);

	private String from;

	private String[] to;

	private EmailSender emailSender;

	/* (non-Javadoc)
	 * @see com.shengpay.bgw.service.router.RunInformationEmailService#sendEmail(java.lang.String)
	 */
	@Override
	public void sendEmail(String operationInfo, RunInfoCollector runInfoCollector) {
		if(!needSendEmail(runInfoCollector)) {
			return;
		}
		
		try {
			emailSender.sendEmail(from, getToAddresses(), getEmailSubject(operationInfo), getEmailBody(operationInfo, runInfoCollector));
		} catch (CheckException e1) {
			logger.error("发送路由报警时发送异常：",e1);
		}
	}

	private boolean needSendEmail(RunInfoCollector runInfoCollector) {
		return runInfoCollector.hasWarnInfo() || runInfoCollector.hasErrorInfo();
	}

	private String[] getToAddresses() {
		return to;
	}

	private String getEmailSubject(String operationInfo) {
		return "【"+IOStreamUtil.getLocalName()+"】发出【"+operationInfo+"】报警！";
	}

	/**
	 * @param operationInfo 
	 * @param runInfoCollector
	 * @param e
	 * @return
	 */
	private String getEmailBody(String operationInfo, RunInfoCollector runInfoCollector) {
		StringBuffer mailBody=new StringBuffer();
		List<RunInfo> infoList = runInfoCollector.getInfoList();
		for (RunInfo runInfo : infoList) {
			mailBody.append("【"+runInfo.getLevelName()+"】：");
			if(StringUtils.notBlank(runInfo.getInfo())) {
				mailBody.append(runInfo.getInfo()+"\r\n");
			}
			if(runInfo.getThrowable()!=null) {
				mailBody.append(ThrowableUtils.getThrowableInfo(runInfo.getThrowable()));
			}
			if(mailBody.capacity()>MAX_MAIL_BODY_LENGTH) {
				break;
			}
		}
		
		return "<pre>"+mailBody+"</pre>";
	}

	public void setEmailSender(EmailSender emailSender) {
		this.emailSender = emailSender;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setTo(String to) {
		this.to = to.split(",");
	}
	
	public static void main(String[] args) {
		System.out.println(new StringBuffer("12").capacity());
	}
}
