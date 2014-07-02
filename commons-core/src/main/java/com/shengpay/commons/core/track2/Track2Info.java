package com.shengpay.commons.core.track2;

import com.shengpay.commons.core.base.BaseObject;

/**
 * @title 二磁道信息对象
 * @description
 * @usage
 * @copyright Copyright 2011 SDO Corporation. All rights reserved.
 * @company SDO Corporation.
 * @author zhaozhijie <zhaozhijie@snda.com>
 * @version
 * @create 2011-4-20 上午10:29:04
 */
public class Track2Info extends BaseObject {
	private static final long serialVersionUID = -5556774057958262444L;
	
	/**
	 * 过期日期格式
	 */
	public final static String EXPIRE_DATE_FORMAT = "yyMM";
	
	/**
	 * 主账号(卡号)
	 */
	private String pan;

	/**
	 * 有效期(YYMM)
	 */
	private String expireDate;

	/**
	 * 服务码(固化为"000")
	 */
	private String serviceCode = "000";

	/**
	 * 私有串
	 */
	private String privateStr;

	public Track2Info() {

	}

	/**
	 * 构造二磁道信息,输入卡号,有效期默认为"0000"
	 * 
	 * @param pan 卡号
	 */
	public Track2Info(String pan) {
		this(pan, "0000");
	}

	/**
	 * 构造二磁道信息,输入卡号、有效期(YYMM)
	 * 
	 * @param pan 卡号
	 * @param expireDate 有效期(YYMM)
	 */
	public Track2Info(String pan, String expireDate) {
		this.pan = pan;
		this.expireDate = expireDate;
		this.privateStr = Track2Util.genericPrivateStr();
	}

	/**
	 * @return the pan
	 */
	public String getPan() {
		return pan;
	}

	/**
	 * @param pan the pan to set
	 */
	public void setPan(String pan) {
		this.pan = pan;
	}

	/**
	 * @return the expDate
	 */
	public String getExpireDate() {
		return expireDate;
	}

	/**
	 * @param expireDate the expDate to set
	 */
	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	/**
	 * @return the serviceCode
	 */
	public String getServiceCode() {
		return serviceCode;
	}

	/**
	 * @return the privateStr
	 */
	public String getPrivateStr() {
		return privateStr;
	}

	/**
	 * @param privateStr the privateStr to set
	 */
	public void setPrivateStr(String privateStr) {
		this.privateStr = privateStr;
	}
}
