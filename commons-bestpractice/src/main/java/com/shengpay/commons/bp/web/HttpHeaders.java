package com.shengpay.commons.bp.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.HttpServletResponse;

/**
 * For e.g. of firefox reqest and response header
 * 
 * Response Headers
 * 
 * Server Apache-Coyote/1.1
 * 
 * Pragma No-cache
 * 
 * Cache-Control no-cache
 * 
 * Expires Thu, 01 Jan 1970 08:00:00 CST
 * 
 * X-Powered-By Servlet 2.4; JBoss-4.0.4.GA (build: CVSTag=JBoss_4_0_4_GA
 * date=200605151000)/Tomcat-5.5
 * 
 * Content-Type application/json;charset=UTF-8
 * 
 * Transfer-Encoding chunked
 * 
 * Date Thu, 24 Jan 2008 04:32:41 GMT
 * 
 * Request Headers
 * 
 * Host localhost:13100
 * 
 * User-Agent Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.11)
 * Gecko/20071127 Firefox/2.0.0.11
 * 
 * Accept
 * text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain
 * ;q=0.8,image/png,;q=0.5
 * 
 * Accept-Language en,zh-cn;q=0.8,en-gb;q=0.5,zh;q=0.3
 * 
 * Accept-Encoding gzip,deflate
 * 
 * Accept-Charset gb2312,utf-8;q=0.7,;q=0.7
 * 
 * Keep-Alive 300
 * 
 * Connection keep-alive
 * 
 * Content-Type application/x-www-form-urlencoded
 * 
 * Referer http://localhost:13100/portal/
 * 
 * Cookie JSESSIONID=42E2F2F3BE8292F177F0E526E8B88753;
 * JSESSIONIDSSO=0BB1494D7BEB14CA04695AB1D93AEAD2
 * 
 * 
 * 
 * @author kuguobing@snda.com
 * 
 */
public final class HttpHeaders {

	public static final String ACCEPT = "ACCEPT";

	public static final String ACCEPT_LANGUAGE = "Accept-Language";

	public static final String ACCEPT_ENCODING = "Accept-Encoding";

	public static final String ACCEPT_CHARSET = "Accept-Charset";

	public static final String CACHE_CONTROL = "Cache-Control";

	public static final String CONTENT_DISPOSITION = "Content-Disposition";

	public static final String CONTENT_TYPE = "Content-Type";

	public static final String COOKIE = "Cookie";

	public static final String IF_MODIFIED_SINCE = "If-Modified-Since";

	public static final String LAST_MODIFIED = "Last-Modified";

	public static final String PRAGMA = "Pragma";

	public static final String EXPIRES = "expires";

	public static final String PUBLIC = "public";

	public static final String USER_AGENT = "User-Agent";

	public static void clearResponseCache(final HttpServletResponse response) {
		response.setHeader(CACHE_CONTROL, "no-cache");
		response.setHeader(PRAGMA, "private");
		response.setHeader(EXPIRES, "0");
	}

	/* @see http://www.webmasterworld.com/forum88/5891.htm */
	public static void setLimitedCache(final HttpServletResponse response,
			int seconds) {
		response.setHeader(CACHE_CONTROL, "max-age=" + seconds
				+ ", must-revalidate");
		// cache control "private" for downloads to work in IE
		response.setHeader(PRAGMA, "private");
	}

	public static void setLastModified(final HttpServletResponse response,
			Date modified) {
		response.setHeader("Last-Modified", getHTTPDate(modified));
	}

	public static String getHTTPDate(Date date) {
		// Sun, 06 Nov 1994 08:49:37 GMT
		SimpleDateFormat format = new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss zzz");
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		return format.format(date);
	}

}
