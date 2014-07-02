package com.shengpay.commons.core.email;

import java.io.File;
import java.io.Serializable;

/** 
 * 功能:邮件附件   <p> 
 * 用法:
 * @version 1.0
 */ 
public class MailAttachment implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;

	/**
	 * 附件名
	 */
	private String fileName;
	
	/**
	 * 附件
	 */
	private File file;
	
	/**
	 * @param fileName 附件名
	 * @param inputStream 附件
	 */
	public MailAttachment(String fileName,File file) {
		this.fileName = fileName;
		this.file = file;
	}
	public String getFileName() {
		return fileName;
	}
	
	public File getFile() {
		return file;
	}
}
