package com.shengpay.commons.bp.web;

import javax.servlet.http.HttpServletRequest;

/**
 * Detect all kinds of browser and OS from the browser Header,such as
 * IE,FF,Safari,Opera,Navigator
 * 
 * @see http://www.zytrax.com/tech/web/browser_ids.htm for examples.
 */
public final class BrowserChecker {

	// ========================
	// IE Browser detect helper testing

	/**
	 * For e.g. of IE 6.0 Browser
	 * 
	 * Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)
	 * 
	 * === MimeHeaders === accept =
	 * 
	 * accept-language = en,zh-cn;q=0.7,en-gb;q=0.3
	 * 
	 * referer = http://localhost:13100/portal/
	 * 
	 * x-requested-with = XMLHttpRequest
	 * 
	 * accept-encoding = gzip, deflate
	 * 
	 * user-agent = Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)
	 * 
	 * host = localhost:13100
	 * 
	 * connection = Keep-Alive
	 * 
	 * cookie = JSESSIONID=8236CE8A07680B631A5B38D6CAF712D3;
	 * JSESSIONIDSSO=BA1EF74FBEDFC78DA9D019C0439EC258
	 * 
	 * 
	 * @param req
	 * @return
	 */
	public static boolean is_ie(HttpServletRequest req) {
		if (req == null) {
			return false;
		}

		String agent = req.getHeader(HttpHeaders.USER_AGENT);

		if (agent == null) {
			return false;
		}

		agent = agent.toLowerCase();

		if (agent.indexOf("msie") != -1) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean is_ie_4(HttpServletRequest req) {
		if (req == null) {
			return false;
		}

		String agent = req.getHeader(HttpHeaders.USER_AGENT);

		if (agent == null) {
			return false;
		}

		agent = agent.toLowerCase();

		if (is_ie(req) && (agent.indexOf("msie 4") != -1)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean is_ie_5(HttpServletRequest req) {
		if (req == null) {
			return false;
		}

		String agent = req.getHeader(HttpHeaders.USER_AGENT);

		if (agent == null) {
			return false;
		}

		agent = agent.toLowerCase();

		if (is_ie(req) && (agent.indexOf("msie 5.0") != -1)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean is_ie_5_5(HttpServletRequest req) {
		if (req == null) {
			return false;
		}

		String agent = req.getHeader(HttpHeaders.USER_AGENT);

		if (agent == null) {
			return false;
		}

		agent = agent.toLowerCase();

		if (is_ie(req) && (agent.indexOf("msie 5.5") != -1)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean is_ie_5_5_up(HttpServletRequest req) {
		if (is_ie(req) && !is_ie_4(req) && !is_ie_5(req)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean is_ie_6(HttpServletRequest req) {
		if (req == null) {
			return false;
		}

		String agent = req.getHeader(HttpHeaders.USER_AGENT);

		if (agent == null) {
			return false;
		}

		agent = agent.toLowerCase();

		if (is_ie(req) && (agent.indexOf("msie 6.0") != -1)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean is_ie_7(HttpServletRequest req) {
		if (req == null) {
			return false;
		}

		String agent = req.getHeader(HttpHeaders.USER_AGENT);

		if (agent == null) {
			return false;
		}

		agent = agent.toLowerCase();

		if (is_ie(req) && (agent.indexOf("msie 7.0") != -1)) {
			return true;
		} else {
			return false;
		}
	}

	// ======================
	// FireFox FF browser testing helper

	/**
	 * For e.g. of Firefox2.0.0.11 browser
	 * 
	 * Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.11)
	 * Gecko/20071127 Firefox/2.0.0.11
	 * 
	 * === MimeHeaders === host = localhost:13100
	 * 
	 * user-agent = Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.11)
	 * Gecko/20071127 Firefox/2.0.0.11
	 * 
	 * accept =
	 * text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text
	 * /plain;q=0.8,image/png;q=0.5
	 * 
	 * accept-language = en,zh-cn;q=0.8,en-gb;q=0.5,zh;q=0.3
	 * 
	 * accept-encoding = gzip,deflate
	 * 
	 * accept-charset = gb2312,utf-8;q=0.7,;q=0.7
	 * 
	 * keep-alive = 300
	 * 
	 * connection = keep-alive
	 * 
	 * x-requested-with = XMLHttpRequest
	 * 
	 * referer = http://localhost:13100/portal/
	 * 
	 * cookie = JSESSIONID=55082566A6EF30A19E16F4F57888E259;
	 * JSESSIONIDSSO=040D7BB8360389645FA776154AA6E5E1
	 * 
	 * @param req
	 * @return
	 */
	public static boolean is_Firefox(HttpServletRequest req) {
		if (req == null) {
			return false;
		}

		String agent = req.getHeader(HttpHeaders.USER_AGENT);

		if (agent == null) {
			return false;
		}

		agent = agent.toLowerCase();

		if ((agent.indexOf("firefox") != -1)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean is_mozilla(HttpServletRequest req) {
		if (req == null) {
			return false;
		}

		String agent = req.getHeader(HttpHeaders.USER_AGENT);

		if (agent == null) {
			return false;
		}

		agent = agent.toLowerCase();

		if ((agent.indexOf("mozilla") != -1)
				&& (agent.indexOf("spoofer") == -1)
				&& (agent.indexOf("compatible") == -1)
				&& (agent.indexOf("opera") == -1)
				&& (agent.indexOf("webtv") == -1)
				&& (agent.indexOf("hotjava") == -1)) {

			return true;
		} else {
			return false;
		}
	}

	// ============================
	// Opera browser testing helper

	/**
	 * For e.g of Opera 9.24 browser
	 * 
	 * Opera/9.24 (Windows NT 5.1; U; zh-cn)
	 * 
	 * === MimeHeaders === x-requested-with = XMLHttpRequest
	 * 
	 * user-agent = Opera/9.24 (Windows NT 5.1; U; zh-cn)
	 * 
	 * host = localhost:13100
	 * 
	 * accept = text/html, application/xml;q=0.9, application/xhtml+xml,
	 * image/png, image/jpeg, image/gif, image/x-xbitmap, ;q=0.1
	 * 
	 * accept-language = zh-CN,zh;q=0.9,en;q=0.8
	 * 
	 * accept-charset = iso-8859-1, utf-8, utf-16,;q=0.1
	 * 
	 * accept-encoding = deflate, gzip, x-gzip, identity, ;q=0
	 * 
	 * referer = http://localhost:13100/portal/
	 * 
	 * cookie = JSESSIONID=6727E6EE1B7EEF40EFE9BB2A32E9C218;
	 * JSESSIONIDSSO=555381588E6EDA2F72A7F39C8346ADC8
	 * 
	 * cookie2 = $Version=1
	 * 
	 * connection = Keep-Alive, TE
	 * 
	 * te = deflate, gzip, chunked, identity, trailers
	 * 
	 * @param req
	 * @return
	 */
	public static boolean is_Opera(HttpServletRequest req) {
		if (req == null) {
			return false;
		}

		String agent = req.getHeader(HttpHeaders.USER_AGENT);

		if (agent == null) {
			return false;
		}

		agent = agent.toLowerCase();

		if ((agent.indexOf("opera") != -1)) {
			return true;
		} else {
			return false;
		}
	}

	// ===========================
	// Netsacpe Navigator browser testing helper

	/**
	 * For e.g. of Netscape Navagator 9 browser
	 * 
	 * Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.10pre)
	 * Gecko/20071127 Firefox/2.0.0.10 Navigator/9.0.0.4
	 * 
	 * === MimeHeaders === host = localhost:13100
	 * 
	 * user-agent = Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US;
	 * rv:1.8.1.10pre) Gecko/20071127 Firefox/2.0.0.10 Navigator/9.0.0.4
	 * 
	 * accept =
	 * text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text
	 * /plain;q=0.8,image/png;q=0.5
	 * 
	 * accept-language = en-us,en;q=0.5
	 * 
	 * accept-encoding = gzip,deflate
	 * 
	 * accept-charset = ISO-8859-1,utf-8;q=0.7;q=0.7
	 * 
	 * keep-alive = 300
	 * 
	 * connection = keep-alive
	 * 
	 * x-requested-with = XMLHttpRequest
	 * 
	 * referer = http://localhost:13100/portal/
	 * 
	 * cookie = JSESSIONID=B63FFBB35121E18F00F030A0D7786F8F;
	 * JSESSIONIDSSO=691540BAF26966B5052A6D5FA6F59E07
	 * 
	 * 
	 * @param req
	 * @return
	 */
	public static boolean is_ns_4(HttpServletRequest req) {
		if (req == null) {
			return false;
		}

		String agent = req.getHeader(HttpHeaders.USER_AGENT);

		if (agent == null) {
			return false;
		}

		agent = agent.toLowerCase();

		if (!is_ie(req) && (agent.indexOf("mozilla/4.") != -1)) {
			return true;
		} else {
			return false;
		}
	}

	// ====================
	// apple mac os x safari browser testing helper

	/**
	 * Safari
	 * 
	 * OS X browser now in Release 2.0 (with V 3 in Beta) for you MAC users -
	 * and now available for the iPhone and iPod as well as more earth bound
	 * devices. Following explanation of browser geneology from Robert Johnson
	 * "Safari uses, and Apple helps develop KHTML (which is what Konqueror
	 * embeds). KHTML is in WebCore, which is part of AppleWebKit. AppleWebKit
	 * is available to any app on the Mac. OmniWeb abandoned their own rendering
	 * engine for AppleWebKit with version 4.5.". So there you go. Apparently
	 * the choice of KHTML instead of Gecko was a wee bit contentious. Safari
	 * apparently now runs on Windows as well as OS X.
	 * 
	 * Mozilla/5.0 (Macintosh; U; Intel Mac OS X; en-gb) AppleWebKit/523.10.6
	 * (KHTML, like Gecko) Version/3.0.4 Safari/523.10.6
	 * 
	 * Explanation: Safari 3.0.4 on Mac OS 10.5.1 Intel. String from Jason
	 * Mayfield-Lewis - Thanks.
	 * 
	 * Mozilla/5.0 (iPod; U; CPU like Mac OS X; en) AppleWebKit/420.1 (KHTML,
	 * like Gecko) Version/3.0 Mobile/3A100a Safari/419.3
	 * 
	 * Explanation: Safari 3.0 for the iPod touch. String from Greg Mcguiness -
	 * Thanks.
	 * 
	 * Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML,
	 * like Gecko) Version/3.0 Mobile/1C28 Safari/419.3
	 * 
	 * Explanation: Safari 3.0 for the iPhone. String from Greg Mcguiness -
	 * Thanks.
	 * 
	 * Mozilla/5.0 (Macintosh; U; Intel Mac OS X; en) AppleWebKit/522.11.1
	 * (KHTML, like Gecko) Version/3.0.3 Safari/522.12.1
	 * 
	 * Explanation: Safari 3.0.3 for Intel version of iMac. String from Greg
	 * Mcguiness - Thanks.
	 * 
	 * Mozilla/5.0 (Windows; U; Windows NT 5.1; bg) AppleWebKit/522.13.1 (KHTML,
	 * like Gecko) Version/3.0.2 Safari/522.13.1
	 * 
	 * Explanation: Safari 3.0.2 beta for Windows XP. String from Deyan Mavrov -
	 * Thanks.
	 * 
	 * Mozilla/4.0 (compatible; Mozilla/4.0; Mozilla/5.0; Mozilla/6.0;
	 * Safari/431.7; Macintosh; U; PPC Mac OS X 10.6 Leopard; AppleWebKit/421.9
	 * (KHTML, like Gecko) )
	 * 
	 * Explanation: Safari browser V2 on OS X (10.6 Leopard). String from ? -
	 * Thanks.
	 * 
	 * Mozilla/5.0 (Windows; U; Windows NT 5.1; ru) AppleWebKit/522.11.3 (KHTML,
	 * like Gecko) Version/3.0 Safari/522.11.3
	 * 
	 * Explanation: Safari browser V 3.0 Beta for Windows XP SP2 . String from
	 * Vadim Korneyko - Thanks.
	 * 
	 * Mozilla/5.0 (Macintosh; U; Intel Mac OS X; en) AppleWebKit/419.3 (KHTML,
	 * like Gecko) Safari/419.3
	 * 
	 * Explanation: Safari browser V 2.o.4 with Beta for OS X . String from
	 * Robert Carter - Thanks.
	 * 
	 * Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en) AppleWebKit/418.8 (KHTML,
	 * like Gecko) Safari/419.3
	 * 
	 * Explanation: Safari browser 2.0.4 for MAC OS X (10.4.7) . String from
	 * Peter Tax - Thanks.
	 * 
	 * Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en) AppleWebKit/417.9 (KHTML,
	 * like Gecko) Safari/417.8
	 * 
	 * Explanation: Safari browser 2.0.3 for MAC OS X (10.4.4) . String from
	 * Pavel Sochnev - Thanks.
	 * 
	 * Mozilla/5.0 (Macintosh; U; Intel Mac OS X; en) AppleWebKit/417.3 (KHTML,
	 * like Gecko) Safari/417.2
	 * 
	 * Explanation: Safari browser 2.0 for MAC OS X (10.4.4 build) . String from
	 * Tim Johnsen - Thanks.
	 * 
	 * Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en) AppleWebKit/412 (KHTML, like
	 * Gecko) Safari/412
	 * 
	 * Explanation: Safari browser 2.0 for MAC OS X (10.4.1 build 8B15) . String
	 * from Robert Vawter - Thanks.
	 * 
	 * Mozilla/5.0 (Macintosh; U; PPC Mac OS X; fr-fr) AppleWebKit/312.5.1
	 * (KHTML, like Gecko) Safari/312.3.1
	 * 
	 * Explanation: Safari 1.3.1 on 1.3.9 after after Security update 2005-008 .
	 * String from Herve B - Thanks.
	 * 
	 * Mozilla/5.0 (Macintosh; U; PPC Mac OS X; fr-fr) AppleWebKit/312.5 (KHTML,
	 * like Gecko) Safari/312.3
	 * 
	 * Explanation: Safari 1.3.1 (v312.3) 10.3.9 = last update on last version
	 * of Panther a.k.a all people who can't update to Tiger 10.4 !! . String
	 * from Herve B - Thanks.
	 * 
	 * Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en) AppleWebKit/124 (KHTML, like
	 * Gecko) Safari/125.1
	 * 
	 * Explanation: Safari browser 1.25.1 for MAC OS X. String from Jim Prince -
	 * thanks. If you are into this kind of Safari stuff Jim also keeps a full
	 * list from the early betas on his site.
	 * 
	 * Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en) AppleWebKit/106.2 (KHTML,
	 * like Gecko) Safari/100.1
	 * 
	 * Explanation: Safari browser 1.0 for MAC OS X. (string from Yaso Leon).
	 * 
	 * Mozilla/5.0 (Macintosh; U; PPC Mac OS X; es) AppleWebKit/85 (KHTML, like
	 * Gecko) Safari/85
	 * 
	 * Explanation: Safari browser 1.0 for MAC OS X with spanish language
	 * variant. (string from Robert Johnson).
	 * 
	 * Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en-us) AppleWebKit/74 (KHTML,
	 * like Gecko) Safari/74
	 * 
	 * Explanation: Safari browser build 74 for MAC OS X. (string from Eric
	 * Noel).
	 * 
	 * Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en) AppleWebKit/51 (like Gecko)
	 * Safari/51
	 * 
	 * Explanation: Safari browser for MAC OS X. (string from erik ?, Robert
	 * Seymour and Ken Zirkel).
	 */

	/**
	 * For e.g. of safari 3.04(523.15)beta apple browser
	 * 
	 * Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN) AppleWebKit/523.15
	 * (KHTML, like Gecko) Version/3.0 Safari/523.15
	 * 
	 * === MimeHeaders === accept-encoding = gzip, deflate
	 * 
	 * accept-language = zh-CN
	 * 
	 * x-requested-with = XMLHttpRequest
	 * 
	 * user-agent = Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN)
	 * AppleWebKit/523.15 (KHTML, like Gecko) Version/3.0 Safari/523.15
	 * 
	 * accept = ""
	 * 
	 * referer = http://localhost:13100/portal/
	 * 
	 * cookie = JSESSIONID=5A0984D564E06BE9B9FA4C06925B33E3;
	 * JSESSIONIDSSO=F02C677238698BC9F32759EA78159D38
	 * 
	 * connection = keep-alive
	 * 
	 * host = localhost:13100
	 * 
	 * @param req
	 * @return
	 */
	public static boolean is_safari(HttpServletRequest req) {
		if (req == null) {
			return false;
		}

		String agent = req.getHeader(HttpHeaders.USER_AGENT);

		if (agent == null) {
			return false;
		}

		agent = agent.toLowerCase();

		if (agent.indexOf("safari") != -1) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean is_safari_window(HttpServletRequest req) {
		if (is_safari(req) && is_window(req)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean is_safari_mac(HttpServletRequest req) {
		if (is_safari(req) && is_mac(req)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * Mozilla/5.0 (iPod; U; CPU like Mac OS X; zh-cn) AppleWebKit/420.1 (KHTML,
	 * like Gecko) Version/3.0 Mobile/4A93 Safari/419.3
	 * 
	 * === MimeHeaders === accept-encoding = gzip, deflate
	 * 
	 * connection = keep-alive
	 * 
	 * cache-control = max-age=0
	 * 
	 * host = 10.15.15.81:13100
	 * 
	 * accept-language = zh-cn
	 * 
	 * user-agent = Mozilla/5.0 (iPod; U; CPU like Mac OS X; zh-cn)
	 * AppleWebKit/420.1 (KHTML, like Gecko) Version/3.0 Mobile/4A93
	 * Safari/419.3
	 * 
	 * cookie = JSESSIONIDSSO=5C294D989FA9AE6859163BE46B2B40F2;
	 * JSESSIONID=18F0914ACF0D86141DD0C5022028E865
	 * 
	 * accept =
	 * text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text
	 * /plain;q=0.8,image/png;q=0.5
	 * 
	 * 
	 * Explanation: Safari 3.0 for the iPod touch. using new ipod testing
	 * 
	 * @param req
	 * @return
	 */
	public static boolean is_safari_ipod(HttpServletRequest req) {
		if (req == null) {
			return false;
		}

		String agent = req.getHeader(HttpHeaders.USER_AGENT);

		if (agent == null) {
			return false;
		}

		agent = agent.toLowerCase();

		if (agent.indexOf("safari") != -1 && agent.indexOf("ipod") != -1
				&& agent.indexOf("mac os x") != -1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML,
	 * like Gecko) Version/3.0 Mobile/1C28 Safari/419.3
	 * 
	 * Explanation: Safari 3.0 for the iPhone. String from Greg Mcguiness -
	 * Thanks.
	 * 
	 * 
	 * @param req
	 * @return
	 */
	public static boolean is_safari_iphone(HttpServletRequest req) {
		if (req == null) {
			return false;
		}

		String agent = req.getHeader(HttpHeaders.USER_AGENT);

		if (agent == null) {
			return false;
		}

		agent = agent.toLowerCase();

		if (agent.indexOf("safari") != -1 && agent.indexOf("iphone") != -1
				&& agent.indexOf("mac os x") != -1) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean is_wml(HttpServletRequest req) {
		if (req == null) {
			return false;
		}

		String accept = req.getHeader(HttpHeaders.ACCEPT);

		if (accept == null) {
			return false;
		}

		accept = accept.toLowerCase();

		if (accept.indexOf("wap.wml") != -1) {
			return true;
		} else {
			return false;
		}
	}

	// ====================
	// OS system detect testing helper

	public static boolean is_linux(HttpServletRequest req) {
		String agent = req.getHeader(HttpHeaders.USER_AGENT);

		if (agent == null) {
			return false;
		}

		agent = agent.toLowerCase();

		if (agent.matches(".*linux.*")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean is_window(HttpServletRequest req) {
		String agent = req.getHeader(HttpHeaders.USER_AGENT);

		if (agent == null) {
			return false;
		}

		agent = agent.toLowerCase();

		if (agent.matches(".*windows.*")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean is_mac(HttpServletRequest req) {
		String agent = req.getHeader(HttpHeaders.USER_AGENT);

		if (agent == null) {
			return false;
		}

		agent = agent.toLowerCase();

		if (agent.matches(".*mac os x.*")) {
			return true;
		} else {
			return false;
		}
	}

}
