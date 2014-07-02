package com.shengpay.commons.bp.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

/**
 * 
 * @Title: RepresentationHelper.java
 * @Description: View视图展现辅助类
 * @author kuguobing<kuguobing@snda.com>
 * @date 2011-7-12 下午06:32:01
 * @version V1.0
 */
public class ViewHelper {
    public static final String CONTENT_TYPE_SISX = "x-epoc/x-sisx-app";

    public static final String CONTENT_TYPE_SIS = "application/vnd.symbian.install";

    public static final String CONTENT_TYPE_OCTET_STREAM = "application/octet-stream";

    // vcard vcf
    public static final String CONTENT_TYPE_VCARD = "text/x-vcard";

    // iCalendar event
    public static final String CONTENT_TYPE_CALENDAR = "text/calendar";

    private ViewHelper() {
    }

    /**
     * 
     * @param request
     * @param response
     * @param fis
     * @param asName
     * @param download
     * @throws Exception
     */
    public static void asFile(HttpServletRequest request, HttpServletResponse response, InputStream fis, int length,
            String contentType, String asName, boolean download) throws Exception {
        /* @see http://www.webmasterworld.com/forum88/5891.htm */
        response.setHeader("Cache-Control", "max-age=60, must-revalidate");
        // cache control "private" for downloads to work in IE
        response.setHeader("Pragma", "private");

        // filename encode some url char " " "/" and so on...
        String downloadFileName = asName;
        /**
         * Chinese localization hanlder(non-english)
         * 
         * @see http://levi.bloghome.cn/posts/79424.html
         *      http://www.javaeye.com/topic/50414
         *      http://www.blogjava.net/zamber/archive/2006/09/14/69752.html
         *      http://support.microsoft.com/?kbid=816868
         * 
         */
        if (BrowserChecker.is_ie(request)) {
            downloadFileName = URLEncoder.encode(downloadFileName, "UTF-8");
        } else if (BrowserChecker.is_mozilla(request)) {
            // downloadFileName = MimeUtility.encodeWord(downloadFileName);
            downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO-8859-1");
        }

        // if file open in new window then "attachement",otherwise "inline"
        response.setHeader("Content-Disposition", (download ? "attachment" : "inline") + ";filename=\""
                + downloadFileName + "\"");
        if (contentType != null && !contentType.trim().equals("")) {
            response.setContentType(contentType);
        } else {
            response.setContentType(CONTENT_TYPE_OCTET_STREAM);
        }
        try {
            response.setContentLength(length > 0 ? length : fis.available());
            // get response output stream
            OutputStream sos = response.getOutputStream();
            IOUtils.copy(fis, sos);
            // flush and close sos
            sos.flush();
            sos.close();
        } catch (IOException e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(fis);
        }
    }

    public static void asStream(HttpServletResponse response, byte[] bytes, String contentType) throws Exception {
        if (bytes == null) {
            bytes = new byte[0];
        }
        asStream(response, new ByteArrayInputStream(bytes), contentType);
    }

    public static void asStream(HttpServletResponse response, InputStream is, String contentType) throws Exception {
        if (contentType != null && !contentType.trim().equals("")) {
            response.setContentType(contentType);
        } else {
            response.setContentType(CONTENT_TYPE_OCTET_STREAM);
        }
        // populate view model to input stream object,then write to servlet
        // output stream
        // @see http://www.webmasterworld.com/forum88/5891.htm
        // cache control "private" for downloads to work in IE
        response.setHeader("Cache-Control", "max-age=60, must-revalidate");
        response.setContentLength(is.available());

        try {
            // get response output stream
            ServletOutputStream sos = response.getOutputStream();

            IOUtils.copy(is, sos);

            // flush and close sos
            sos.flush();
            sos.close();
        } catch (IOException e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
}
