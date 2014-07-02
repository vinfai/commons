/**
 * 
 */
package com.shengpay.commons.core.runinfo;

import java.util.Date;

import com.shengpay.commons.core.base.BaseObject;

/**
 * 运行信息
 * @copyright	Copyright 2012 SDP Corporation. All rights reserved.
 * @author		lindongcheng <lindongcheng@snda.com>
 * @create		2012-11-22 下午01:21:17
 */

public class RunInfo extends BaseObject{
	
	/**
	 * 信息级别：普通信息 
	 */
	public static final int LEVEL_INFO=0;
	
	/**
	 * 信息级别：普通信息 
	 */
	public static final int LEVEL_WARN=1;
	
	/**
	 * 信息级别：普通信息 
	 */
	public static final int LEVEL_ERROR=2;
	
	
	/**
	 * 信息内容
	 */
	private String info;
	
	/**
	 * 发生时间
	 */
	private Date date=new Date();
	
	private Throwable throwable;
	
	/**
	 * 信息级别
	 */
	private int level=LEVEL_INFO;

	public RunInfo(Throwable t) {
		this(null,t);
	}
	/**
	 * @param info
	 * @param level
	 */
	public RunInfo(String info, Throwable t) {
		this(info,LEVEL_ERROR);
		this.throwable=t;
	}
	public RunInfo(String info, int level) {
		this.info=info;
		this.level=level;
	}
	
	/**
	 * @param info
	 * @param level
	 */
	public RunInfo(String info) {
		this(info,LEVEL_INFO);
	}

	/**
	 * 获取【{@link #info info}】
	 * @return 类型：String
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * 获取【{@link #date date}】
	 * @return 类型：Date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * 获取【{@link #level level}】
	 * @return 类型：int
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * 获取【{@link #level level}】
	 * @return 类型：int
	 */
	public String getLevelName() {
		switch (level) {
			case LEVEL_INFO:
				return "运行信息：";
			case LEVEL_WARN:
				return "警告信息：";
			case LEVEL_ERROR:
				return "错误信息：";
			default:
				return "未知信息级别【"+level+"】";
		}
	}
	public Throwable getThrowable() {
		return throwable;
	}
}
