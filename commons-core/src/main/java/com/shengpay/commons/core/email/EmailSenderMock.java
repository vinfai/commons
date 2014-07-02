/**
 * 
 */
package com.shengpay.commons.core.email;

import com.shengpay.commons.core.exception.CheckException;

/**
 * 模拟实现
 * @description
 * @author Lincoln
 */

public class EmailSenderMock implements EmailSender {

	@Override
	public void sendEmail(String from, String to, String subject,
			String mailBody) throws CheckException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendEmail(String from, String[] to, String subject,
			String mailBody) throws CheckException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendEmail(Mail mail) throws CheckException {
		// TODO Auto-generated method stub
		
	}



}
