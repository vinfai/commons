/**
 * 
 */
package com.shengpay.commons.jmscustomer.email;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.shengpay.commons.core.email.EmailSender;
import com.shengpay.commons.core.exception.CheckException;


/**
 * 
 * @copyright	Copyright 2012 SDP Corporation. All rights reserved.
 * @author		lindongcheng <lindongcheng@snda.com>
 * @create		2012-11-22 下午03:55:34
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/ac_commons-jmscustomer_test_cjh.xml"})
//@ContextConfiguration(locations = { "classpath:/META-INF/ac_commons-jmscustomer_test_jndi.xml", "classpath:/META-INF/ac_commons-jmscustomer2.xml"})
//@ContextConfiguration(locations = { "classpath:/META-INF/ac_commons-jmscustomer_test_jndi.xml", "classpath:/META-INF/ac_commons-jmscustomer2.xml"})
public class EmailSenderImplTest {
	
	@Resource
	private EmailSender emailSender;
	
	private String from=null;
	private String to="lindongcheng@snda.com";
	private String subject="测试邮件";
	private String mailBody="测试邮件内容";
	
	@Test
	public void testBasicFlow() throws CheckException, InterruptedException {
		emailSender.sendEmail(from, to, new Date().toString(), mailBody);
//		Thread.sleep(Long.MAX_VALUE);
	}
	
//	@Test
	public void testCustomJms() throws InterruptedException {
	}
}
