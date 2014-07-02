package com.shengpay.commons.jmscustomer.email;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.shengpay.commons.core.email.EmailSender;
import com.shengpay.commons.core.email.Mail;
import com.shengpay.commons.core.email.MailAttachment;
import com.shengpay.commons.core.exception.CheckException;
import com.shengpay.commons.core.utils.CollectionUtils;
import com.shengpay.commons.core.utils.StringUtils;

/**
 * JAVA MAIL实现
 */
@Service("emailSenderImpl")
public class EmailSenderImpl implements EmailSender {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(EmailSenderImpl.class);
	
	private JavaMailSenderImpl sender; // 实际的发送实现
	
	@Value("${mailSmtpUser}")
	private String mailSmtpUser;
	
	@Value("${mailSmtpHost}")
	private String mailSmtpHost;
	
	@Value("${mailSmtpPort}")
	private String mailSmtpPort;
	
	@Value("${mailSmtpConnectiontimeout}")
	private String mailSmtpConnectiontimeout;
	
	@Value("${mailSmtpTimeout}")
	private String mailSmtpTimeout;
	
	@Value("${mailSmtpFrom}")
	private String mailSmtpFrom;
	
	@Value("${mailSmtpAuth}")
	private String mailSmtpAuth;
	
	@Value("${mailSmtpPassword}")
	private String mailSmtpPassword;
	
	@Override
	public void sendEmail(String from, String to, String subject, String mailBody) throws CheckException {
		sendEmail(from, new String[] { to }, subject, mailBody);
	}
	
	@Override
	public void sendEmail(String from, String[] to, String subject, String mailBody) throws CheckException {
		// 构造MAIL对象
		Mail mail = new Mail();
		mail.setFrom(from);
		mail.setTo(to);
		mail.setSubject(subject);
		mail.setContent(mailBody);
		sendEmail(mail);
		
	}
	
	@Override
	public void sendEmail(Mail mail) throws CheckException {
		// 检查必要参数
		if (mail == null) {
			throw new CheckException("mail can not be null.");
		}
		if (CollectionUtils.isEmpty(mail.getTo())) {
			throw new CheckException("收件人不能为空");
		}
		MimeMessageHelper helper = null;
		try {
			helper = new MimeMessageHelper(sender.createMimeMessage(), true, "UTF-8");
			
			// 发件人
			if (mail.getFrom() != null) {
				if (mail.getFromName() == null) {
					helper.setFrom(mail.getFrom());
				} else {
					helper.setFrom(mail.getFrom(), mail.getFromName());
				}
				
			}
			// 收件人
			helper.setTo(mail.getTo());
			
			// 抄送人
			if (mail.getCc() != null) {
				helper.setCc(mail.getCc());
			}
			
			// 密送人
			if (mail.getBcc() != null) {
				helper.setBcc(mail.getBcc());
			}
			
			// 邮件主题
			String subject = mail.getSubject();
			helper.setSubject(subject != null ? subject : "");
			
			// 邮件内容
			String content = mail.getContent();
			helper.setText(content != null ? content : "", mail.isHtmlFormat());
			
			// 附件
			if (mail.getAttachments() != null) {
				for (MailAttachment attachment : mail.getAttachments()) {
					helper.addAttachment(attachment.getFileName(), attachment.getFile());
				}
			}
			
			// 发送时间
			helper.setSentDate(new Date());
		} catch (UnsupportedEncodingException e) {
			logger.error("sendEmail(Mail)", e);
			
			throw new CheckException(e);
		} catch (MessagingException e) {
			logger.error("sendEmail(Mail)", e);
			
			throw new CheckException(e);
		}
		
		// 发送
		try {
			sender.send(helper.getMimeMessage());
			logger.info("发送邮件【"+mail.getSubject()+"】给【"+StringUtils.getString(mail.getTo())+"】");
		} catch (MailException e) {
			logger.error("sendEmail(Mail)", e);
			
			throw new CheckException(e);
		}
		
	}
	
	@PostConstruct
	public void init() {
		
		Properties pros = new Properties();
		if (StringUtils.notBlank(mailSmtpUser)) {
			pros.setProperty("mail.smtp.user", mailSmtpUser);
		}
		if (StringUtils.notBlank(mailSmtpHost)) {
			pros.setProperty("mail.smtp.host", mailSmtpHost);
		}
		if (StringUtils.notBlank(mailSmtpPort)) {
			pros.setProperty("mail.smtp.port", mailSmtpPort);
		}
		if (StringUtils.notBlank(mailSmtpConnectiontimeout)) {
			pros.setProperty("mail.smtp.connectiontimeout", mailSmtpConnectiontimeout);
		}
		if (StringUtils.notBlank(mailSmtpTimeout)) {
			pros.setProperty("mail.smtp.timeout", mailSmtpTimeout);
		}
		if (StringUtils.notBlank(mailSmtpFrom)) {
			pros.setProperty("mail.smtp.from", mailSmtpFrom);
		}
		if (StringUtils.notBlank(mailSmtpAuth)) {
			pros.setProperty("mail.smtp.auth", mailSmtpAuth);
		}
		
		sender = new JavaMailSenderImpl();
		sender.setJavaMailProperties(pros);
		sender.setPassword(mailSmtpPassword);
	}
	
}
