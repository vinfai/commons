package com.shengpay.commons.monitor.utils;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * IP工具类
 * 
 * @author yangjianhua
 * 
 */
public class IPUtils {

	/**
	 * 获取本机所有IP
	 * 
	 * @return
	 * @throws SocketException
	 */
	public static List<String> getServerIps() throws SocketException {
		List<String> ips = new ArrayList<String>();
		// 根据网卡取本机配置的IP
		Enumeration<?> e1 = (Enumeration<?>) NetworkInterface
				.getNetworkInterfaces();
		while (e1.hasMoreElements()) {
			NetworkInterface ni = (NetworkInterface) e1.nextElement();
			Enumeration<?> e2 = ni.getInetAddresses();
			while (e2.hasMoreElements()) {
				InetAddress ia = (InetAddress) e2.nextElement();
				if (ia instanceof Inet6Address)
					continue;
				String ip = ia.getHostAddress();
				ips.add(ip);
			}
		}
		return ips;
	}

	/**
	 * 获取本机除127.0.0.1外的所有IP
	 * 
	 * @return
	 * @throws SocketException
	 */
	public static List<String> getServerIpsExceptLocalHost()
			throws SocketException {
		List<String> allIps = getServerIps();
		List<String> ips = new ArrayList<String>();
		for (String ip : allIps) {
			if (!StringUtils.equalsIgnoreCase(ip, "127.0.0.1")) {
				ips.add(ip);
			}
		}
		return ips;
	}

	/**
	 * 是否是本机IP
	 * 
	 * @param compareIp
	 * @return
	 * @throws SocketException
	 */
	public static boolean isThisServer(String compareIp) throws SocketException {
		List<String> ips = IPUtils.getServerIps();
		for (String ip : ips) {
			if (StringUtils.equalsIgnoreCase(ip, compareIp))
				return true;
		}
		return false;
	}

	/**
	 * 得到客户端真实IP地址。若存在反向代理header：http_x_forwarded_for，则取其值，否则，取header:
	 * remote_addr
	 * 
	 * @return
	 */
	/*
	public static String getClientIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (StringUtils.isEmpty(ip)
				|| StringUtils.equalsIgnoreCase(ip, "unknown")) {
			ip = request.getRemoteAddr();
		}

		return ip;
	}
*/
	
	/**
	 * 返回服务器IP字符串
	 * 
	 * @return
	 * @throws SocketException
	 */
	public static String getServerIPsStr() throws SocketException {
		StringBuilder sb = new StringBuilder();
		List<String> ips = IPUtils.getServerIpsExceptLocalHost();
		for (String ip : ips) {
			sb.append(ip);
			sb.append("   ");
		}
		return sb.toString();
	}
	
	public static String getServerHostAndIPsStr() throws SocketException, UnknownHostException {
		String hostName = InetAddress.getLocalHost().getHostName();
		StringBuilder sb = new StringBuilder();
		sb.append(hostName + ": ");
		List<String> ips = getServerIps();
		for (String ip : ips) {
			sb.append(ip);
			sb.append(";");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
}
