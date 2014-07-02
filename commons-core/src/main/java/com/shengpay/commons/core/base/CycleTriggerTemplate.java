/**
 * 
 */
package com.shengpay.commons.core.base;

import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

/**
 * 定时加载模板类
 * @copyright	Copyright 2012 SDP Corporation. All rights reserved.
 * @author		lindongcheng <lindongcheng@snda.com>
 * @create		2012-11-16 上午10:26:44
 */

public abstract class CycleTriggerTemplate {
	
	@PostConstruct
	public void init() {
		trigger();
		int loadPeriod = getCyclePeriod();
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				trigger();
			}
			
		},loadPeriod,loadPeriod);
	}

	/**
	 * 执行加载操作
	 */
	protected abstract void trigger();
	
	/**
	 * 获取加载周期
	 */
	protected abstract int getCyclePeriod();
}
