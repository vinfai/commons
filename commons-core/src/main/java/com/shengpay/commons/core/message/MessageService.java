package com.shengpay.commons.core.message;

import java.util.Map;

import com.shengpay.commons.core.exception.CheckException;


/** 
 * 功能:  系统消息发送服务 <p> 
 * 用法:
 * @version 1.0
 */ 
public interface MessageService {
	
/**
 * 根据消息模板表中的消息编号取得消息模板，填充，发送
 * 
 * @param bmtCode  消息模板表中的消息编号
 * @param params 填充模板内容的参数
 * @param to  消息的接收人
 * @throws CheckException 模板不存在，或是发送消息出现异常
 */
public void sendMessage(String bmtCode,Map params, String... to) throws CheckException;

}
