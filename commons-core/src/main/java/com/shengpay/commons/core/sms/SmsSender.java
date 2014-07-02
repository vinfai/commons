package com.shengpay.commons.core.sms;

import com.shengpay.commons.core.exception.CheckException;

/**
 * 
 * @description
 * @author Lincoln
 */
public interface SmsSender {

	/**
	 * 向单个手机发送短信
	 * 
	 * @param mobilePhone 手机号
	 * @param message 短信内容
	 * @return 发送是否成功
	 * @throws CheckException 
	 */
	void sendSms(String mobilePhone, String message) throws CheckException;

	/**
	 * 向多个手机发送短信
	 * 
	 * @param mobilePhones 手机列表
	 * @param message 短信内容
	 * @return 发送是否成功
	 * @throws CheckException 
	 */
	void sendSms(String[] mobilePhones, String message) throws CheckException;

}