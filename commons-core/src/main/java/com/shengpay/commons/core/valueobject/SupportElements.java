/**
 * 
 */
package com.shengpay.commons.core.valueobject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.shengpay.commons.core.base.BaseObject;
import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.utils.CollectionUtils;
import com.shengpay.commons.core.utils.StringUtils;

/**
 * 支持元素
 * @copyright	Copyright 2012 SDP Corporation. All rights reserved.
 * @author		lindongcheng <lindongcheng@snda.com>
 * @create		2012-11-22 上午10:12:56
 */
public class SupportElements extends BaseObject{

	/**
	 * 支持的元素
	 */
	private Set<String> supportElementsList=new HashSet<String>();
	
	/**
	 * 不支持的元素
	 */
	private Set<String> notSupportElementsList=new HashSet<String>();
	
	/**
	 * 支持全部元素
	 */
	private Boolean supportAll=null;

	/* (non-Javadoc)
	 * @see com.shengpay.bgw.commons.valueobject.SupportElements#isSupport(java.lang.String)
	 */
	
	public boolean isSupport(String aElement) {
		if(Boolean.TRUE.equals(supportAll)) {
			return true;
		}
		
		String ele=aElement!=null?aElement.toLowerCase():null;
		if(notSupportElementsList.size()>0) {
			return !notSupportElementsList.contains(ele);
		}
		
		return supportElementsList.contains(ele);
	}
	
	/* (non-Javadoc)
	 * @see com.shengpay.bgw.commons.valueobject.SupportElements#isOverlap(com.shengpay.bgw.commons.valueobject.SupportElementsImpl)
	 */
	
	public boolean isOverlap(SupportElements se) {
		if(!(se instanceof SupportElements)) {
			throw new SystemException("类型【"+se+"】不匹配！");
		}
		
		//如果其中一个为支持全部，则会重合
		SupportElements sei=(SupportElements) se;
		if(Boolean.TRUE.equals(supportAll) || Boolean.TRUE.equals(sei.supportAll)) {
			return true;
		}
		
		if(notSupportElementsList.size()>0 && sei.notSupportElementsList.size()>0) {
			return true;
		}
		
		if(notSupportElementsList.size()>0) {
			return !notSupportElementsList.equals(sei.supportElementsList);
		}
		
		if(sei.notSupportElementsList.size()>0) {
			return !sei.notSupportElementsList.equals(supportElementsList);
		}
		
		return CollectionUtils.isOverlap(supportElementsList,sei.supportElementsList) || CollectionUtils.isOverlap(sei.supportElementsList,supportElementsList);
	}

	
	public boolean isNull() {
		return supportAll==null && supportElementsList.size()==0 && notSupportElementsList.size()==0;
	}

	
	public Collection<String> getSupportElementsList() {
		return Collections.unmodifiableCollection(supportElementsList);
	}

	public void setSupportElementsList(Set<String> supportElementsList) {
		if(supportElementsList!=null) {
			this.supportElementsList = CollectionUtils.toLowerCase(supportElementsList);
		}else {
			this.supportElementsList = new HashSet<String>();
		}
	}
	
	public void addSupportElement(String element) {
		this.supportElementsList.add(StringUtils.toLowerCase(element));
	}
	
	public void addNotSupportElement(String element) {
		this.notSupportElementsList.add(StringUtils.toLowerCase(element));
	}

	
	public Collection<String> getNotSupportElementsList() {
		return Collections.unmodifiableCollection(notSupportElementsList);
	}

	public void setNotSupportElementsList(Set<String> notSupportElementsList) {
		if(notSupportElementsList!=null) {
			this.notSupportElementsList =CollectionUtils.toLowerCase(notSupportElementsList);
		}else {
			this.notSupportElementsList =new HashSet<String>();
		}
	}

	
	public Boolean getSupportAll() {
		return supportAll;
	}

	public void setSupportAll(Boolean supportAll) {
		this.supportAll = supportAll;
	}
}
