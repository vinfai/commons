package com.shengpay.commons.web.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

import com.shengpay.commons.core.exception.CheckException;



/**
 * 多线程httpclient工具
 * @description	 
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDO Corporation.
 * @author 		wangjinhua
 * @version		$Id: UserLoginCtrl.java,v 1.2 2009-12-4 下午02:31:10 chenjianxiao Exp $
 * @create		2009-12-4 下午02:31:10
 *
 */
public class MultiHttpClientUtils {
	
		static MultiThreadedHttpConnectionManager  connectionManager = new MultiThreadedHttpConnectionManager();
		
		/**
		 * get方式请求
		 * @param url		                     请求url
		 * @param connTimeOut		连接超时时间(秒)
		 * @param reqTimeOut		请求超时时间(秒)
		 * @return          
		 */
		public static byte[] getRequest(String url,Integer connTimeOut,Integer reqTimeOut) throws CheckException {
			HttpClient client = new HttpClient(connectionManager);
			if(connTimeOut!=null){
				client.getHttpConnectionManager().getParams().setConnectionTimeout(connTimeOut*1000);
			}
			if(reqTimeOut!=null){
				client.getParams().setSoTimeout(reqTimeOut*1000);
			}
			byte[] resData = null;
			HttpMethod method = new GetMethod(url);
			int httpCode = 0;
			BufferedInputStream in = null;
			ByteArrayOutputStream out = null;
			try {
				httpCode = client.executeMethod(method);
				resData = method.getResponseBody();
				in = new BufferedInputStream(method.getResponseBodyAsStream());
				out = new ByteArrayOutputStream(4*1024);
				byte[] temp = new byte[4*1024];
				int size = 0;
				while ((size = in.read(temp)) != -1) {
		            out.write(temp, 0, size);
		        }
				resData = out.toByteArray();
			} catch (HttpException e) {
				throw new CheckException(
						"http req cause HttpException【请求url:" + url + ", httpCode:" + httpCode + " 】:", e);
			} catch (IOException e) {
				throw new CheckException(
						"http req cause IOException【请求url:" + url + ", httpCode:" + httpCode + " 】:", e);
			} finally {
				if (method != null) {
					method.releaseConnection();
				}
				if(in !=null){
					try {
						in.close();
					} catch (IOException e) {
					}
				}
				if(out !=null){
					try {
						out.close();
					} catch (IOException e) {
					}
				}
			}
			return resData;
		}
		
	public static String postForDFSLocation(String url, byte[] bytes, Integer connTimeOut,
			Integer reqTimeOut) throws CheckException {
		HttpClient client = new HttpClient(connectionManager);
		if (connTimeOut != null) {
			client.getHttpConnectionManager().getParams().setConnectionTimeout(connTimeOut * 1000);
		}
		if (reqTimeOut != null) {
			client.getParams().setSoTimeout(reqTimeOut * 1000);
		}

		PostMethod method = new PostMethod(url);
		RequestEntity requestEntity = new ByteArrayRequestEntity(bytes);
		method.setRequestEntity(requestEntity);
		try {
			int httpCode = client.executeMethod(method);
			if (201 == httpCode) {
				Header responseHeader = method.getResponseHeader("Location");
				return responseHeader.getValue();
			} else {
				throw new CheckException("http post DFS error code " + httpCode);
			}
		} catch (Exception e) {
			throw new CheckException("http post exception", e);
		}
		finally {
			if (method != null) {
				try {
					method.releaseConnection();
				} catch (Exception e2) {
					// omit exception
				}
			}
		}
	}

}
