/**
 * 
 */
package com.shengpay.commons.springtools.quartz;

import java.io.Serializable;

import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

/**
 * 将spring封装quartz的工厂类添加上Serializable，以便集群多机支持
 * @copyright	Copyright 2012 SDP Corporation. All rights reserved.
 * @author		lindongcheng <lindongcheng@snda.com>
 * @create		2012-1-16 下午02:19:41
 */

public class MethodInvokingJobDetailFactoryBean4SDP extends MethodInvokingJobDetailFactoryBean implements Serializable {

}
