package com.shengpay.commons.core.runinfo;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.shengpay.commons.core.email.EmailSender;
import com.shengpay.commons.core.exception.CheckException;
import com.shengpay.commons.core.utils.IOStreamUtil;
import com.shengpay.commons.core.utils.ThrowableUtils;

public class RunInfoEmailSenderImplTest {

	private RunInfoEmailSenderImpl runInfoEmailSender;
	private EmailSender emailSender;
	private String from="test@snda.com";
	private String to="lindongcheng@snda.com,nijia@snda.com";
	private Exception exception1 = new Exception("exceptionMsg1");
	private Exception exception2 = new Exception("exceptionMsg2");

	
	@Before
	public void setUp() throws Exception {
		emailSender=mock(EmailSender.class);
		
		runInfoEmailSender=new RunInfoEmailSenderImpl();
		runInfoEmailSender.setEmailSender(emailSender);
		runInfoEmailSender.setFrom(from);
		runInfoEmailSender.setTo(to);
	}

	@Test
	public void test() throws CheckException {
		runInfoEmailSender.sendEmail("emailSubject", getRunInfoCollector());
		verify(emailSender).sendEmail(from, new String[] {"lindongcheng@snda.com","nijia@snda.com"}, "【"+IOStreamUtil.getLocalName()+"】发出【emailSubject】报警！", mailBody());
	}

	private String mailBody() {
		return "<pre>【运行信息：】：infoMsg\r\n"
				+ "【错误信息：】："+ ThrowableUtils.getThrowableInfo(exception1)
				+ "【警告信息：】：warnMsg\r\n"
				+ "【错误信息：】：errorMsg\r\n"
				+ "【错误信息：】：exceptionMsg\r\n"+ ThrowableUtils.getThrowableInfo(exception2)
				+"</pre>";
	}

	private RunInfoCollector getRunInfoCollector() {
		RunInfoCollector runInfoCollector=new RunInfoCollector();
		runInfoCollector.info("infoMsg");
		runInfoCollector.add(new RunInfo(exception1));
		runInfoCollector.warn("warnMsg");
		runInfoCollector.error("errorMsg");
		runInfoCollector.add(new RunInfo("exceptionMsg",exception2));
		return runInfoCollector;
	}

}
