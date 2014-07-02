package com.shengpay.commons.web.base;

import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;



/**
 * 标签库基础类
 * @description
 * @author Lincoln
 */
public abstract class BaseIteratorTaglib extends BaseTaglib {
	
	/**
	 * 支付品牌迭代器
	 */
	private Iterator<?> iterator;

	/**
	 * 迭代值存放在request中的属性名(默认为var)
	 */
	private String valueName="var";

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		//取得支付品牌列表迭代器
		List<?> valueList = getValueList();
		iterator = valueList.iterator();
		
		//处理当前状态
		return processValue(EVAL_BODY_INCLUDE);
	}

	protected abstract List<?> getValueList();

	/**
	 * 执行单次输出后的操作
	 * @see javax.servlet.jsp.tagext.TagSupport#doAfterBody()
	 */
	@Override
	public int doAfterBody() throws JspException {
		return processValue(EVAL_BODY_AGAIN);
	}

	/**
	 * 标签完成后的操作:是否资源
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		iterator=null;
		return super.doEndTag();
	}

	/**
	 * 判断当前状态,若有输出项,则取得并输出,若无输出项,则跳出标签体
	 * @return
	 */
	private int processValue(int operation) {
		//若没有列表项,则跳过标签
		if(!iterator.hasNext()) {
			return SKIP_BODY;
		}
		
		//若有列表项,这将当前支付品牌信息存放到request指定属性中
		Object prEO = iterator.next();
		getRequest().setAttribute(valueName, prEO);
		return operation;
	}

	public String getValueName() {
		return valueName;
	}

	public void setValueName(String valueName) {
		this.valueName = valueName;
	}
}
