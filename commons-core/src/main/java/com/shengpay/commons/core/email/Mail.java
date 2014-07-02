
package com.shengpay.commons.core.email;

import java.io.Serializable;
import java.util.regex.Pattern;


import com.shengpay.commons.core.base.BaseObject;
import com.shengpay.commons.core.utils.StringUtils;



/** 
 * 功能: 封装邮件对象  <p> 
 * 用法:
 * @version 1.0
 */ 
public class Mail extends BaseObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 发件人
	 */
	private String from;
	/**
	 * 发件人(显示)
	 */
	private String fromName;
	/**
	 * 收件人
	 */
	private String[] to;
	/**
	 * 抄送
	 */
	private String[] cc;
	/**
	 * 秘密抄送
	 */
	private String[] bcc;
	/**
	 * 邮件主题
	 */
	private String subject;
	/**
	 * 邮件内容
	 */
	private String content;
	/**
	 * 附件
	 */
	private  MailAttachment[] attachments;
	
	/**
	 * 是否以HTML格式发送
	 */
	boolean isHtmlFormat = true;
	

	
	public String getFrom() {
		return from;
	}



	public void setFrom(String from) {
		this.from = from;
	}



	public String getFromName() {
		return fromName;
	}



	public void setFromName(String fromName) {
		this.fromName = fromName;
	}



	public String[] getTo() {
		return to;
	}



	public void setTo(String[] to) {
		for (String addr : to) {
			addTo(addr);
		}
		
	}
     
	/**
	 * @param to 收件人
	 */
	public void addTo(String to) {
		 this.to = addAddress(this.to ,to);
	}


	public String[] getCc() {
		return cc;
	}



	public void setCc(String[] cc) {
		for (String addr : cc) {
			addCc(addr);
		}
	}
	
	/**
	 * @param to 抄送人
	 */
	public void addCc(String cc) {
		 this.cc = addAddress(this.cc ,cc);
	}


	public String[] getBcc() {
		return bcc;
	}



	public void setBcc(String[] bcc) {
		for (String addr : bcc) {
			addBcc(addr);
		}
	}
	

	/**
	 * @param to 密送人
	 */
	public void addBcc(String bcc) {
		 this.bcc = addAddress(this.bcc ,bcc);
	}

	
	

	public String getSubject() {
		return subject;
	}



	public void setSubject(String subject) {
		this.subject = subject;
	}



	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}



	public MailAttachment[] getAttachments() {
		return attachments;
	}



	public void setAttachments(MailAttachment[] attachments) {
		this.attachments = attachments;
	}



	public boolean isHtmlFormat() {
		return isHtmlFormat;
	}



	public void setHtmlFormat(boolean isHtmlFormat) {
		this.isHtmlFormat = isHtmlFormat;
	}
	/**
	 * 简单校验邮件格式
	 * @param address
	 * @return
	 */
	private boolean availableEmailAddress(String address){
		if (StringUtils.isBlank(address))
			return false;
		return Pattern.matches(".+@.+\\.+.*",address);
	}
	
	/**
	 * @param src
	 * @param address
	 * @return
	 */
	private String[] addAddress(String[] src, String address) {
		if (availableEmailAddress(address)) {
			return (String[]) add(src, address);
		} else {
			return src;
		}
	}



	private String[] add(String[] src, String address) {
		if(src==null) {
			return new String[] {address};
		}
		
		String[] newArr=new String[src.length+1];
		for (int i = 0; i < src.length; i++) {
			newArr[i]=src[i];
		}
		newArr[src.length]=address;
		return newArr;
	}
	

}
