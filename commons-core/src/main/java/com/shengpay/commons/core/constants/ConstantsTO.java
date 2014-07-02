/**
 * 
 */
package com.shengpay.commons.core.constants;

import java.util.ArrayList;
import java.util.List;

import com.shengpay.commons.core.base.BaseObject;
import com.shengpay.commons.core.base.LogOutAnn;

/**
 * 常量信息对象
 * @description
 * @usage
 * @copyright Copyright 2010 SDO Corporation. All rights reserved.
 * @company SDOCorporation.
 * @author LinDongCheng <lindongcheng@snda.com>
 * @version $Id: ConstantsTO.java,v 1.0 2010-4-17 下午05:05:54 lindc Exp $
 * @create 2010-4-17 下午05:05:54
 */

public class ConstantsTO extends BaseObject {

	/**
	 * 所属类名
	 */
	private String className;

	/**
	 * 常量所属域名
	 */
	private String fieldName;

	/**
	 * 常量值
	 */
	private String fieldValue;

	/**
	 * 常量所属类型
	 */
	private String constantsType;

	/**
	 * 常量的显示名
	 */
	private String constantsName;

	/**
	 * 常量的反向名
	 */
	private String constantsReverseName;

	/**
	 * 父域名
	 */
	private String parentFieldName;

	/**
	 * 父常量信息
	 */
	@LogOutAnn(disableLogOut=true)
	private ConstantsTO parentConstantsTO;

	/**
	 * 子常量列表
	 */
	@LogOutAnn(disableLogOut=true)
	private List<ConstantsTO> clientList = new ArrayList<ConstantsTO>();

	/**
	 * 转出方转账类型
	 */
	private String fromFundType;

	/**
	 * 转入方转账类型
	 */
	private String toFundType;

	/**
	 * 转出方账户类型
	 * 
	 * @return
	 */
	private String fromAbType;

	/**
	 * 转入方账户类型
	 * 
	 * @return
	 */
	private String toAbType;
	
	/**
	 * 转出方转账类型
	 */
	private String fromFundTypeFieldName;
	
	/**
	 * 转入方转账类型
	 */
	private String toFundTypeFieldName;
	
	/**
	 * 转出方账户类型
	 * 
	 * @return
	 */
	private String fromAbTypeFieldName;
	
	/**
	 * 转入方账户类型
	 * 
	 * @return
	 */
	private String toAbTypeFieldName;

	public String getFromFundTypeFieldName() {
		return fromFundTypeFieldName;
	}

	public void setFromFundTypeFieldName(String fromFundTypeFieldName) {
		this.fromFundTypeFieldName = fromFundTypeFieldName;
	}

	public String getToFundTypeFieldName() {
		return toFundTypeFieldName;
	}

	public void setToFundTypeFieldName(String toFundTypeFieldName) {
		this.toFundTypeFieldName = toFundTypeFieldName;
	}

	public String getFromAbTypeFieldName() {
		return fromAbTypeFieldName;
	}

	public void setFromAbTypeFieldName(String fromAbTypeFieldName) {
		this.fromAbTypeFieldName = fromAbTypeFieldName;
	}

	public String getToAbTypeFieldName() {
		return toAbTypeFieldName;
	}

	public void setToAbTypeFieldName(String toAbTypeFieldName) {
		this.toAbTypeFieldName = toAbTypeFieldName;
	}

	/**
	 * 转出方是否需要查询
	 * 
	 * @return
	 */
	private boolean fromQueryFlag;

	/**
	 * 转入方是否需要查询
	 * 
	 * @return
	 */
	private boolean toQueryFlag;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String field) {
		this.fieldName = field;
	}

	public String getConstantsName() {
		return constantsName;
	}

	public void setConstantsName(String name) {
		this.constantsName = name;
	}

	public String getConstantsReverseName() {
		return constantsReverseName;
	}

	public void setConstantsReverseName(String nameReverse) {
		this.constantsReverseName = nameReverse;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String value) {
		this.fieldValue = value;
	}

	public String getParentFieldName() {
		return parentFieldName;
	}

	public void setParentFieldName(String parentField) {
		this.parentFieldName = parentField;
	}

	public String getConstantsType() {
		return constantsType;
	}

	public void setConstantsType(String type) {
		this.constantsType = type;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<ConstantsTO> getClientList() {
		return clientList;
	}

	public void addClient(ConstantsTO client) {
		clientList.add(client);
	}

	public ConstantsTO getParentConstantsTO() {
		return parentConstantsTO;
	}

	public void setParentConstantsTO(ConstantsTO parentConstantsTO) {
		this.parentConstantsTO = parentConstantsTO;
	}

	public String getFromFundType() {
		return fromFundType;
	}

	public void setFromFundType(String fromFundType) {
		this.fromFundType = fromFundType;
	}

	public String getToFundType() {
		return toFundType;
	}

	public void setToFundType(String toFundType) {
		this.toFundType = toFundType;
	}

	public String getFromAbType() {
		return fromAbType;
	}

	public void setFromAbType(String fromAbType) {
		this.fromAbType = fromAbType;
	}

	public String getToAbType() {
		return toAbType;
	}

	public void setToAbType(String toAbType) {
		this.toAbType = toAbType;
	}

	public boolean isFromQueryFlag() {
		return fromQueryFlag;
	}

	public void setFromQueryFlag(boolean fromQueryFlag) {
		this.fromQueryFlag = fromQueryFlag;
	}

	public boolean isToQueryFlag() {
		return toQueryFlag;
	}

	public void setToQueryFlag(boolean toQueryFlag) {
		this.toQueryFlag = toQueryFlag;
	}

	public void setClientList(List<ConstantsTO> clientList) {
		this.clientList = clientList;
	}

	@Override
	public ConstantsTO clone() {
		ConstantsTO cloneBean = new ConstantsTO();
		cloneBean.className=className;
		cloneBean.clientList=clientList;
		cloneBean.constantsName=constantsName;
		cloneBean.constantsReverseName=constantsReverseName;
		cloneBean.constantsType=constantsType;
		cloneBean.fieldName=fieldName;
		cloneBean.fieldValue=fieldValue;
		cloneBean.fromAbType=fromAbType;
		cloneBean.fromAbTypeFieldName=fromAbTypeFieldName;
		cloneBean.fromFundType=fromFundType;
		cloneBean.fromFundTypeFieldName=fromFundTypeFieldName;
		cloneBean.fromQueryFlag=fromQueryFlag;
		cloneBean.parentConstantsTO=parentConstantsTO;
		cloneBean.toQueryFlag=toQueryFlag;
		cloneBean.toFundTypeFieldName=toFundTypeFieldName;
		cloneBean.toFundType=toFundType;
		cloneBean.toAbTypeFieldName=toAbTypeFieldName;
		cloneBean.toAbType=toAbType;
		cloneBean.parentFieldName=parentFieldName;
		return cloneBean;
	}
	
	public static void main(String[] args) {
		ConstantsTO a=new ConstantsTO();
		a.setClassName("aaa");
		
		ConstantsTO b=new ConstantsTO();
		b.setClassName("bbb");
		
		a.setParentConstantsTO(b);
		b.setParentConstantsTO(a);
		
		System.out.println(a);
	}
}
