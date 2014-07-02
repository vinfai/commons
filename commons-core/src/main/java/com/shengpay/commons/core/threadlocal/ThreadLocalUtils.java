/**
 * 
 */
package com.shengpay.commons.core.threadlocal;

import java.util.HashMap;
import java.util.Map;

/**
 * 当前线程工具类
 * @description
 * @author Lincoln
 */

public class ThreadLocalUtils {
	
	/**
	 * 本地线程变量名:当前用户所属客户端IP
	 */
	private static final String TLN_CLIENT_IP = "TLN_IP";
	/**
     * 本地线程变量名:当前用户所属客户端IP对应地区
     */
    private static final String TLN_CLIENT_AREA = "TLN_AREA";
	
	/**
	 * 本地线程变量名:当前用户所属渠道常量
	 */
	private static final String TLN_CLIENT_CHANNEL = "TLN_CHANNEL";
	
	/**
	 * 本地线程变量名:当前用户实体对象
	 */
	private static final String TLN_USER_VO = "TLN_USER_VO";

	/**
	 * 本地线程变量名:当前用户所属HTTP SESSION信息
	 */
	private static final String TLN_HTTP_SESSION = "TLN_HTTP_SESSION";

	/**
	 * 保持当前线程独有信息的对象
	 */
	private static final ThreadLocal<Map<Object, Object>> threadLocal = new ThreadLocal<Map<Object, Object>>();

	/**
	 * 往当前线程中存放信息
	 * 
	 * @param key
	 * @param value
	 */
	public static void setValue(Object key, Object value) {
		Map<Object, Object> threadLocalValueMap = threadLocal.get();
		if (threadLocalValueMap == null) {
			threadLocalValueMap = new HashMap<Object, Object>();
			threadLocal.set(threadLocalValueMap);
		}
		threadLocalValueMap.put(key, value);
	}

	/**
	 * 从当前线程中取得信息
	 * 
	 * @param key
	 * @return
	 */
	public static Object getValue(Object key) {
		Map<Object, Object> threadLocalValueMap = threadLocal.get();
		if (threadLocalValueMap == null) {
			return null;
		} else {
			return threadLocalValueMap.get(key);
		}
	}

	/**
	 * 取得当前用户名称
	 * 
	 * @return
	 */
	public static String getUserName() {
	    UserVO userVO = getUserVO();
		if(userVO == null){
	        return null; 
	    }
		return userVO.getUserName();
	}

	/**
	 * 取得当前用户编号
	 * 
	 * @return
	 */
	public static Long getUserId() {
	    UserVO userVO = getUserVO();
		if(userVO == null){
	        return null; 
	    }
		return userVO.getUserId();
	}
	/**
	 * 取得当前用户类型
	 * 
	 * @return
	 */
	public static String getUserType() {
	    UserVO userVO = getUserVO();
		if(userVO == null){
            return null; 
        }
		return userVO.getUserType();
	}
	/**
	 * 取得当前用户状态
	 * 
	 * @return
	 */
	public static String getUserStatus() {
	    UserVO userVO = getUserVO();
		if(userVO == null){
            return null; 
        }
		return userVO.getUserStatus();
	}
	
	/**
	 * 设置客户端IP
	 * 
	 * @return
	 */
	public static void setClientIp(String clientIp) {
		setValue(TLN_CLIENT_IP,clientIp);
	}
	/**
	 * 取得客户端IP
	 * 
	 * @return
	 */
	public static String getClientIp() {
	    Object clientIp = getValue(TLN_CLIENT_IP);
	    if(clientIp == null){
	        return null;
	    }
		return (String) clientIp;
	}
    /**
     * 取得客户端MP
     * 
     * @return
     */
    public static String getClientMp() {
	    UserVO userVO = getUserVO();
		if(userVO == null){
            return null; 
        }
        return userVO.getUserMp();
    }
    
    /**
     * 取得用户EMAIL
     * 
     * @return
     */
    public static String getUserEmail() {
	    UserVO userVO = getUserVO();
		if(userVO == null){
            return null; 
        }
        return userVO.getUserEmail();
    }


	/**
	 * @param userLoginVO
	 */
	public static void setUserVO(UserVO userLoginVO) {
		setValue(TLN_USER_VO, userLoginVO);
	}

	/**
	 * @param userLoginVO
	 */
	public static UserVO getUserVO() {
		return (UserVO) getValue(TLN_USER_VO);
	}
	
	/**
	 * @param userLoginVO
	 */
	public static String getUserLoginName() {
		UserVO userVO = getUserVO();
		if(userVO==null) {
			return null;
		}
		return userVO.getLoginName();
	}
	
	/**
	 * @param userLoginVO
	 */
	public static String getUserPayPassword() {
		UserVO userVO = getUserVO();
		if(userVO==null) {
			return null;
		}
		return userVO.getPayPassword();
	}

	/**
	 * 设置 客户端使用的渠道
	 * @param clientChannel
	 */
	public static void setClientChannel(String clientChannel) {
		setValue(TLN_CLIENT_CHANNEL,clientChannel);
	}
	
	/**
	 * 取得 客户端使用的渠道
	 * @return
	 */
	public static String getClientChannel() {
	    Object clientChannel = getValue(TLN_CLIENT_CHANNEL);
	    if(clientChannel == null){
	        return null;
	    }
		return (String) clientChannel;
	}
	
	/**
     * 设置 客户端登录ip所对应的地区信息
     * @param clientChannel
     */
    public static void setClientArea(String clientArea) {
        setValue(TLN_CLIENT_AREA,clientArea);
    }
    
    /**
     * 取得 客户端登录ip所对应的地区信息
     * @return
     */
    public static String getClientArea() {
        Object clientArea = getValue(TLN_CLIENT_AREA);
        if(clientArea == null){
            return null;
        }
        return (String) clientArea;
    }

	/**
	 * 设置当前用户编号
	 * @param userId
	 */
	public static void setUserId(Long userId) {
		UserVO userVO = getUserVO();
		if(userVO!=null){
			userVO.setUserId(userId);
		}else{
			userVO=new UserVO();
			userVO.setUserId(userId);
			setUserVO(userVO);
		}
		
	}
}
