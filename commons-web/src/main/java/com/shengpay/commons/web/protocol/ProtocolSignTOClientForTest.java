/**
 * 
 */
package com.shengpay.commons.web.protocol;

import com.shengpay.commons.core.base.BaseObject;

/**
 * 议对象列表型属性的子对象
 * @description 
 * @usage  
 * @copyright Copyright 2010 SDO Corporation. All rights reserved.
 * @company  SDOCorporation.
 * @author  lindongchengonlindongchengng <lindongcheng@SSDOom>
 * @version  $Id: ProtocolSignTOClientForTest.java,v 1.2 2010lindongcheng 下午05:32:19 lindongchengExp $
 * @create  2010-8-5 下午05:32:19
 */
public class ProtocolSignTOClientForTest extends BaseObject {

	/**
	 * 子的A属性
	 */
	private String name;
	
	/**
	 * 子的B属性 
	 */
	private String num;

	public String getName() {
		return name;
	}

	public void setName(String ca) {
		this.name = ca;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String cb) {
		this.num = cb;
	}
}
