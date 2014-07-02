package com.shengpay.commons.springtools.aop;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.shengpay.commons.core.email.EmailSender;
import com.shengpay.commons.core.exception.BusinessException;
import com.shengpay.commons.core.exception.CheckException;
import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.sms.SmsSender;
import com.shengpay.commons.core.utils.DateTimeUtils;
import com.shengpay.commons.core.utils.ThrowableUtils;

/**
 * 异常监听AOP
 * @description 当系统发生异常时使用短信/邮寄等方式通知相关人员
 * @author Lincoln
 */
public class ExceptionMonitorAOP implements MethodInterceptor {

	/**
	 * 短信发送器
	 */
	private SmsSender smsSenderHandler;

	/**
	 * 邮件发送器
	 */
	private EmailSender emailSender;

	/**
	 * 异常通知手机号数组
	 */
	private String[] notifyMpArr;

	/**
	 * 保存近期异常的列表(该列表定长,超出部分将被删除)
	 */
	private List<Throwable> exceptionList;

	/**
	 * 异常信息邮件发送人地址
	 */
	private String fromEmail;

	/**
	 * 异常信息邮件收件人地址
	 */
	private String[] toEmails;

	/**
	 * 本级ip
	 */
	private InetAddress localhostAddress;

	/**
	 * 短信最大长度
	 */
	private int smsMaxLength;
	
	/**
	 * 监听开关
	 */
	private boolean monitorSwitch=true;

	/**
	 * 发送短信消息开关
	 */
	private boolean sendSmsSwitch=true;
	
	/**
	 * 发送邮件消息开关
	 */
	private boolean sendEmailSwitch=true;
	
	/**
	 * 通知监控部门开关
	 */
	private boolean notifySwitch=true;
	
	/**
	 * 通知监控部门服务
	 */
	private SdoExceptionMonitor sdoExceptionMonitorService;
	
	/**
	 * 初始化拦截器
	 */
	public void init() {
		try {
			localhostAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			throw new SystemException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	public Object invoke(MethodInvocation mi) throws Throwable {
		//如果关闭监听,则直接返回处理结果
		if(!monitorSwitch){
			return mi.proceed();
		}
		
		try {
			return mi.proceed();
		} catch (Throwable e) {
			Object this1 = mi.getThis();
			if(this1 instanceof EmailSender || this1 instanceof SmsSender){
				throw e;
			}
			
			if (e.getClass()==BusinessException.class) {
				throw e;
			}

			if (exceptionList.contains(e)) {
				throw e;
			}

			exceptionList.add(e);
			sendNotify(e);
			throw e;
		}
	}

	/**
	 * 发送异常通知
	 * 
	 * @param e
	 */
	private void sendNotify(Throwable e) {
		if(sendSmsSwitch){
			sendSms(e);
		}
		
		if(sendEmailSwitch){
			sendEmail(e);
		}
		if (notifySwitch && sdoExceptionMonitorService != null){
			sdoExceptionMonitorService.reportToMonitor(e);
		}
	}

	/**
	 * 发送邮件通知
	 * 
	 * @param currentTimeMillis
	 * @param e
	 */
	private void sendEmail(Throwable e) {
		if (toEmails == null || toEmails.length == 0) {
			return;
		}

		String bugNum = DateTimeUtils.getCurrentExacDateTime();
		String mailSubject = "系统异常【" + localhostAddress + "】【" + bugNum + "】【" + ThrowableUtils.getRootMessage(e) + "】";
		String mailBody = "<pre>" + ThrowableUtils.getThrowableInfo(e) + "</pre>";
		// emailSender.sendEmail(fromEmail, toEmails, mailBody, null, null, mailSubject, null, null);
		try {
			emailSender.sendEmail(fromEmail, toEmails, mailSubject, mailBody);
		} catch (CheckException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 发送短信通知
	 * 
	 * @param currentTimeMillis
	 * @param e
	 */
	private void sendSms(Throwable e) {
		if (notifyMpArr == null || notifyMpArr.length == 0) {
			return;
		}

		//整理短信内容
		String smsMsg = "【" + ThrowableUtils.getRootMessage(e) + "】";
		if (smsMsg.length() > smsMaxLength) {
			smsMsg = smsMsg.substring(0, smsMaxLength);
		}

		//一一发送短信
		try {
			smsSenderHandler.sendSms(notifyMpArr, smsMsg);
		} catch (CheckException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 获取“smsSenderHandler”(类型：SmsSenderHandler)
	 */
	public SmsSender getSmsSenderHandler() {
		return smsSenderHandler;
	}

	/**
	 * 设置“smsSenderHandler”（类型：SmsSenderHandler）
	 */
	public void setSmsSenderHandler(SmsSender smsSenderHandler) {
		this.smsSenderHandler = smsSenderHandler;
	}

	/**
	 * 获取“notifyMpArr”(类型：String[])
	 */
	public String[] getNotifyMpArr() {
		return notifyMpArr;
	}

	/**
	 * 设置“notifyMpArr”（类型：String[]）
	 */
	public void setNotifyMpArr(String[] notifyMpArr) {
		this.notifyMpArr = notifyMpArr;
	}

	/**
	 * 获取“exceptionList”(类型：List<Throwable>)
	 */
	public List<Throwable> getExceptionList() {
		return exceptionList;
	}

	/**
	 * 设置“exceptionList”（类型：List<Throwable>）
	 */
	public void setExceptionList(List<Throwable> exceptionList) {
		this.exceptionList = exceptionList;
	}

	/**
	 * 获取“emailSender”(类型：EmailSender)
	 */
	public EmailSender getEmailSender() {
		return emailSender;
	}

	/**
	 * 设置“emailSender”（类型：EmailSender）
	 */
	public void setEmailSender(EmailSender emailSender) {
		this.emailSender = emailSender;
	}

	/**
	 * 获取“fromEmail”(类型：String)
	 */
	public String getFromEmail() {
		return fromEmail;
	}

	/**
	 * 设置“fromEmail”（类型：String）
	 */
	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	/**
	 * 获取“toEmails”(类型：String[])
	 */
	public String[] getToEmails() {
		return toEmails;
	}

	/**
	 * 设置“toEmails”（类型：String[]）
	 */
	public void setToEmails(String[] toEmails) {
		this.toEmails = toEmails;
	}

	/**
	 * 获取“smsMaxLength”(类型：int)
	 */
	public int getSmsMaxLength() {
		return smsMaxLength;
	}

	/**
	 * 设置“smsMaxLength”（类型：int）
	 */
	public void setSmsMaxLength(int smsMaxLength) {
		this.smsMaxLength = smsMaxLength;
	}

	public void setMonitorSwitch(boolean monitorSwitch) {
		this.monitorSwitch = monitorSwitch;
	}

	public void setSendSmsSwitch(boolean sendSmsSwitch) {
		this.sendSmsSwitch = sendSmsSwitch;
	}

	public void setSendEmailSwitch(boolean sendEmailSwitch) {
		this.sendEmailSwitch = sendEmailSwitch;
	}
	public void setSdoExceptionMonitorService(
			SdoExceptionMonitor sdoExceptionMonitorService) {
		this.sdoExceptionMonitorService = sdoExceptionMonitorService;
	}
	public void setNotifySwitch(boolean notifySwitch) {
		this.notifySwitch = notifySwitch;
	}
	
	public static void main(String[] args) {
		System.out.println(getLocalIP());
	}

	public static String getLocalIP() {
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
        
        byte[] ipAddr = addr.getAddress();
        String ipAddrStr = "";
        for (int i = 0; i < ipAddr.length; i++) {
        	System.out.println(ipAddr[i]);
            if (i > 0) {
                ipAddrStr += ".";
            }
            ipAddrStr += ipAddr[i] & 0xFF;
        }
        //System.out.println(ipAddrStr);   
        return ipAddrStr;
    }
}
