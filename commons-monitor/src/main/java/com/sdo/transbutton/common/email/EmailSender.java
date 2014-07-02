package com.sdo.transbutton.common.email;

import com.sdo.transbutton.common.exception.CheckException;

/**
 * 邮件发送器
 * @usage		
 */
public interface EmailSender {

	/**
	 * 发送邮件
	 * @param from 发件人
	 * @param to 收件人
	 * @param subject 邮件主题
	 * @param mailBody  邮件内容
	 * @throws CheckException 参数校验失败或发送邮件失败时，抛出此异常
	 */
	void sendEmail(String from, String to, String subject, String mailBody) throws CheckException;
	
	/**
	 * 发送邮件
	 * @param from 发件人
	 * @param to 多个收件人
	 * @param subject 邮件主题
	 * @param mailBody  邮件内容
	 * @throws CheckException 参数校验失败或发送邮件失败时，抛出此异常
	 */
	void sendEmail(String from, String[] to, String subject, String mailBody) throws CheckException;
	
	/**
	 * 发送邮件
	 * @param mail 邮件
	 * @throws CheckException 参数校验失败或发送邮件失败时，抛出此异常
	 */
//	void sendEmail(Mail mail) throws CheckException;


}