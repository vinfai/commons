package com.shengpay.commons.core.valueobject;

import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.shengpay.commons.core.utils.CollectionUtils;
import com.shengpay.commons.core.utils.StringUtils;

public class SupportElementsFactory {
	
	/**
	 * 全部支持
	 */
	private static final String EXPRESSION_ALL = "all";
	
	/**
	 * 元素间分隔符
	 */
	private static final String EXPRESSION_SEPARATOR = ",";
	
	/**
	 * 排除符
	 */
	private static final String EXPRESSION_NOT_SUPPORT = "!";

	private SupportElementsFactory() {}
	
	private static SupportElementsFactory instance=new SupportElementsFactory();
	
	public static SupportElementsFactory getInstance() {
		return instance;
	}
	
	/**
	 * 构造
	 * @param elements
	 * @return
	 */
	public SupportElements makeBySupportElementsList(String[] elements) {
		SupportElements se=new SupportElements();
		se.setSupportElementsList(CollectionUtils.arrayToSet(elements));
		return se;
	}

	/**
	 * 
	 * @param elements
	 * @return
	 */
	public SupportElements makeByNotSupportElementsList(String[] elements) {
		SupportElements se=new SupportElements();
		se.setNotSupportElementsList(CollectionUtils.arrayToSet(elements));
		return se;
	}

	/**
	 * @param supportElementsStr
	 */
	public SupportElements makeByExpression(String supportElementsStr) {
		SupportElements se=new SupportElements();
		if(StringUtils.isBlank(supportElementsStr)) {
			return se;
		}
		
		
		initializeByExpression(se,supportElementsStr.trim());
		return se;
	}
	
	protected void initializeByExpression(SupportElements se, String supportElementsStr) {
		StringTokenizer st=new StringTokenizer(supportElementsStr,EXPRESSION_SEPARATOR);
		while(st.hasMoreTokens()) {
			putElement(se,st.nextToken());
		}
	}

	/**
	 * @param se 
	 * @param issuerStr
	 */
	private void putElement(SupportElements se, String issuerStr) {
		if(StringUtils.isBlank(issuerStr)) {
			return;
		}
		
		if(issuerStr.equalsIgnoreCase(EXPRESSION_ALL)) {
			se.setSupportAll(Boolean.TRUE);
			return;
		}
		
		if(issuerStr.startsWith(EXPRESSION_NOT_SUPPORT)) {
			se.addNotSupportElement(issuerStr.substring(1));
			return;
		}
		
		se.addSupportElement(issuerStr);
	}
	
	public String getExpression(SupportElements se) {
		if(se==null) {
			return null;
		}
		
		if(Boolean.TRUE.equals(se.getSupportAll())) {
			return EXPRESSION_ALL;
		}

		Collection<String> notSupportElementsList = se.getNotSupportElementsList();
		if(notSupportElementsList!=null && notSupportElementsList.size()>0) {
			return getExpressionByElementsList(notSupportElementsList,false);
		}

		Collection<String> supportElementsList = se.getSupportElementsList();
		if(supportElementsList!=null && supportElementsList.size()>0) {
			return getExpressionByElementsList(supportElementsList,true);
		}
		
		return null;
	}

	private String getExpressionByElementsList(Collection<String> elementsList, boolean isSupport) {
		StringBuffer buf=new StringBuffer();
		for (Iterator<String> iterator = elementsList.iterator(); iterator.hasNext();) {
			String notSupportElement = (String) iterator.next();
			if(!isSupport) {
				buf.append(EXPRESSION_NOT_SUPPORT);
			}
			buf.append(notSupportElement);
			if(iterator.hasNext()) {
				buf.append(EXPRESSION_SEPARATOR);
			}
		}
		
		return buf.toString();
	}

}
