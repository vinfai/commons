/**
 * 
 */
package com.shengpay.commons.core.sms;

import org.apache.log4j.Logger;

import com.shengpay.commons.core.exception.CheckException;
import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.utils.StringUtils;

/**
 * 模拟实现
 * @description
 * @author Lincoln
 */

public class SmsSenderMock implements SmsSender {
    /**
     * 系统日志输出句柄
     */
    private final Logger logger = Logger.getLogger(SmsSenderMock.class);

    /**
     * 向单个手机发送短信
     */
    public void sendSms(String mobilePhone, String message) throws CheckException {
        // 参数验证
        if (StringUtils.isBlank(mobilePhone) || StringUtils.isBlank(message)) {
            throw new SystemException("参数不合法：[sendSms(String mobilePhone<" + mobilePhone + ">,String message<" + message + ">)]");
        }

        logger.info("短信模拟器被要求发送给[" + mobilePhone + "]一条短信[" + message + "]");
    }

    /**
     * 向多个手机发送短信
     */
    public void sendSms(String[] mobilePhones, String message) throws CheckException {
        // 参数验证
        if (mobilePhones == null || StringUtils.isBlank(message)) {
            throw new SystemException("参数不合法：[sendSms(String mobilePhones<" + mobilePhones + ">,String message<" + message + ">)]");
        }
        
        for (int i = 0; mobilePhones != null && i < mobilePhones.length; i++) {
            sendSms(mobilePhones[i], message);
        }
    }
}
