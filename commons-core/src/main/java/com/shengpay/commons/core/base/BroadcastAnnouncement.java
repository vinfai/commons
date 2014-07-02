/**
 * 
 */
package com.shengpay.commons.core.base;

import java.util.Map;

/**
 * 广播通知接口
 * @author lindongcheng
 *
 */
public interface BroadcastAnnouncement {

	/**
	 * 发送广播
	 * @param msgType 消息类型编号
	 * @param paramsMap 参数映射
	 */
	void sendBroadcastAnnouncement(String msgType,Map<String,Object> paramsMap);
}
