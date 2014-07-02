package com.shengpay.commons.core.track2;

import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.utils.NumberUtils;

/**
 * @title 二磁道工具类
 * @description
 * @usage
 * @copyright Copyright 2011 SDO Corporation. All rights reserved.
 * @company SDO Corporation.
 * @author zhaozhijie <zhaozhijie@snda.com>
 * @version
 * @create 2011-4-20 上午10:35:49
 */
public class Track2Util {

	private static final String prefix = ";";
	private static final String splitChar = "=";
	private static final String suffix = "?";

	/**
	 * 解析二磁道数据
	 * 
	 * @param track2 二磁道数据
	 * @return 二磁道对象
	 */
	public static final Track2Info getTrack2Info(String track2) {
		if (track2.startsWith(prefix) == false) {
			track2 = prefix + track2;
		}
		int length = track2.indexOf(splitChar);
		if (track2 == null || length < 14 || length > 20) {
			throw new SystemException("Invalid track2 data.");
		}

		Track2Info ti = new Track2Info();
		int offset = 1;
		ti.setPan(track2.substring(offset, length));
		offset += (length);
		ti.setExpireDate(track2.substring(offset, offset + 4));
		offset += 7;
		ti.setPrivateStr(track2.substring(offset, offset + 6));

		return ti;
	}

	/**
	 * 生成二磁道数据
	 * 
	 * @param track2Info 二磁道信息对象
	 * @return 二磁道数据
	 */
	public static final String genericTrack2(Track2Info track2Info) {
		checkInfo(track2Info);

		StringBuffer track2 = new StringBuffer(prefix);
		track2.append(track2Info.getPan());
		track2.append(splitChar);
		track2.append(track2Info.getExpireDate());
		track2.append(track2Info.getServiceCode());
		track2.append(track2Info.getPrivateStr());
		track2.append(suffix);
		return track2.toString();
	}

	private static void checkInfo(Track2Info track2Info) {

		//检查
		if (track2Info == null) {
			throw new SystemException("Null track2 info.");
		}

		//检查卡号
		if (track2Info.getPan() == null || track2Info.getPan().length() > 19 || track2Info.getPan().length() < 13) {
			throw new SystemException("Invalid PAN.");
		}

		//检查有效期
		if (track2Info.getExpireDate() == null) {
			throw new SystemException("Invalid expire date.");
		}

		if (track2Info.getExpireDate().length() != 4) {
			throw new SystemException("Invalid expire date.");
		}

		//检查私有数据
		if (track2Info.getPrivateStr() == null) {
			throw new SystemException("Null private data.");
		}
	}

	/**
	 * 产生一个随机串：6位数字字符
	 * 
	 * @return
	 */
	public static final String genericPrivateStr() {
		int privateNo = NumberUtils.getRandomIntInMax(1000000);
		String privateStr = "000000" + privateNo;
		privateStr = privateStr.substring(privateStr.length() - 6);
		return privateStr;
	}

}
