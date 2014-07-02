package com.shengpay.commons.web.taglib;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.shengpay.commons.core.base.PaginationBaseObject;
import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.web.base.BaseTaglib;
import com.shengpay.commons.web.util.PaginationUtils;


/**
 * 
 * @title 		分页标签
 * @description	
 * @usage		
 * @copyright	Copyright 2009 5173 Corporation. All rights reserved.
 * @company		5173 Corporation.
 * @author		zhongshu <zhongshu@5173.com>
 * @version		$Id: PageTag.java,v 1.2 2009-12-6 下午03:03:08 zhongshu Exp $
 * @create		2009-12-6 下午03:03:08
 */
public class PageTaglib extends BaseTaglib {
	
    /**
     * 日志输出句柄
     */
    private static final Logger logger = Logger.getLogger(PageTaglib.class);
	
	/**
	 * 从查询请求地址解析出的参数信息
	 */
	private Map<String,String> paramMap=new HashMap<String,String>();

	/*
	 * 分页对象名称
	 */
	private String name;
	
	
	@Override
	public int doStartTag() throws JspException {
		// 若无分页信息，则直接返回
		PaginationBaseObject<?> pageObject = PaginationUtils.getPagination(getRequest());
		if (pageObject == null) {
			return SKIP_BODY;
		}
		String requestUrl = getRequestUrl(getRequest());
		
		printPageTable(pageObject, requestUrl);
		return super.doStartTag();
	}


	/**
	 * 输出翻页表格
	 * @param pageObject 翻页对象
	 * @param requestUrl 翻页请求地址
	 */
	private void printPageTable(PaginationBaseObject<?> pageObject, String requestUrl) {
		int totalPageCount = pageObject.getTotalPageCount();
		int totalRowCount = pageObject.getTotalRowCount();
		int pageSize = pageObject.getPageSize();
		int pageNO = pageObject.getPageNO();
		StringBuffer sb = new StringBuffer("<table width='100%' height='30' border='0' align='center' cellpadding='0' cellspacing='0'>");
		sb.append("<tbody><tr><td><div align='right' >共 ").append(totalRowCount).append(" 条 每页显示").append(pageSize).append(" 条 第 ").append(pageNO).append(" 页/共 ").append(totalPageCount + " 页  ");

		if (pageNO != 1 && pageNO != 0 && totalPageCount > 0) {
			sb.append(" <a href='" + appendPageNO(requestUrl, 1) + "'>首页</a>");
		} else {
			sb.append("&nbsp;首页");
		}

		if (pageNO != 1 && pageNO != 0 && totalPageCount > 0) {
			sb.append(" <a href='" + appendPageNO(requestUrl, pageNO - 1) + "'>&nbsp;上一页</a>");

		} else {
			sb.append("&nbsp;上页");
		}

		if (pageNO != totalPageCount && totalPageCount > 0) {
			sb.append(" <a href='" + appendPageNO(requestUrl, pageNO + 1) + "'>&nbsp;下一页</a>");

		} else {
			sb.append("&nbsp;下页");
		}

		if (pageNO != totalPageCount && totalPageCount > 0) {
			sb.append(" <a href='" + appendPageNO(requestUrl, totalPageCount) + "'>&nbsp;尾页</a> ");

		} else {
			sb.append("&nbsp;尾页");
		}
	    sb.append("&nbsp;转到：<input type='text' maxlength='3' id='goPage'  name='goPage' size='1' value=''> 页 <input type='button' name='button' value='GO' onclick='dosubmitGo()'><a id='gotoPage' style='visibility: hidden;' href='" + appendURL(requestUrl) + "'></a></div></td></tr></tbody></table>");
		sb.append("<script language=\"JavaScript\" type=\"text/javascript\">")
		.append("function dosubmitGo(){")
		.append("var a =" + totalPageCount +";")
		.append("var b=document.getElementById('goPage').value;")
		.append("if(b.indexOf('.')>0){")
		.append("alert('请输入整数！');")
		.append("document.getElementById('goPage').value ='';")
		.append("document.getElementById('goPage').focus();")
		.append("return;")
		.append("}")
		.append("if(isNaN(document.getElementById('goPage').value)==true){")
		.append("alert('请输入数字！');")
		.append("document.getElementById('goPage').value ='';")
		.append("document.getElementById('goPage').focus();")
		.append("return;")
		.append("}")
		.append("if(document.getElementById('goPage').value == ''){")
		.append("alert('请输入页号！');")
		.append("document.getElementById('goPage').focus();")
		.append("return;")
		.append("}")
		.append("if(document.getElementById('goPage').value < 1 ){")
		.append("alert('页号不能小于1！');")
		.append("document.getElementById('goPage').value ='';")
		.append("document.getElementById('goPage').focus();")
		.append("return;")
		.append("}")
		.append("if(document.getElementById('goPage').value > a ){")
		.append("alert('输入页号不能大于总页数！');")
		.append("document.getElementById('goPage').value ='';")
		.append("document.getElementById('goPage').focus();")
		.append("return;")
		.append("}")
		.append("if(document.getElementById('gotoPage').href.indexOf('?')>0){")
		.append("window.location.href=document.getElementById('gotoPage').href+document.getElementById('goPage').value")
		.append("}")
		.append("else{")
		.append("window.location.href=document.getElementById('gotoPage').href+document.getElementById('goPage').value")
		.append("}")
		.append("}")
		.append("</script> ");
		try {
			pageContext.getOut().print(sb.toString());
		} catch (IOException e) {
			throw new SystemException("输出分页标签时发生异常");
		}
	}


	/**
	 * 构造翻页url(不包含上下文根)
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getRequestUrl(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		parameterMap.remove(PaginationUtils.RAN_PAGE_NO);
		parameterMap.remove("cashQueryMethod");//因为抵用券资金查询
		StringBuffer buf = new StringBuffer();
		for (Iterator<Entry<String, String[]>> ite = parameterMap.entrySet().iterator(); ite.hasNext();) {
			Entry<String, String[]> paramEntry = ite.next();
			String paramName = paramEntry.getKey();
			String[] paramValueArr = paramEntry.getValue();
			for (int i = 0; i < paramValueArr.length; i++) {
				if (buf.indexOf("?")!=-1) {
					buf.append("&" + paramName + "="+paramValueArr[i] );
				} else {
					buf.append("?" + paramName + "="+paramValueArr[i] );
				}
			}
		}
		return buf.toString();
	}
	
	public static String appendPageNO(String url, int paramValue) {
		if (url.contains("?")) {
			return url + "&" + PaginationUtils.RAN_PAGE_NO + "=" + paramValue;
		} else {
			return url + "?" + PaginationUtils.RAN_PAGE_NO + "=" + paramValue;
		}
	}

	public static String appendURL(String url) {
		if (url.contains("?")) {
			return url + "&" + PaginationUtils.RAN_PAGE_NO + "=" ;
		} else {
			return url + "?" + PaginationUtils.RAN_PAGE_NO + "=" ;
		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
