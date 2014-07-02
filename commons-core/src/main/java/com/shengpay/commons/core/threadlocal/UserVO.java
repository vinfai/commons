package com.shengpay.commons.core.threadlocal;

/**
 * 用户基本信息
 * @description
 * @usage
 * @copyright Copyright 2010 SDO Corporation. All rights reserved.
 * @company SDOCorporation.
 * @author LinDongCheng <lindongcheng@snda.com>
 * @version $Id: UserVO.java,v 1.0 2009-12-6 上午09:41:36 lindc Exp $
 * @create 2009-12-6 上午09:41:36
 */
public class UserVO {
    /**
     * 用户标识
     */
    private Long userId;

    /**
     * 支付密码
     */
    private String payPassword;
    /**
     * 支付密码
     */
    private String loginPassword;

    /**
     * 登录名
     */
    private String loginName;
    
    
    /**
     * 数字id
     */
    private String sdId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 用户状态
     */
    private String userStatus;

    /**
     * 用户手机号
     */
    private String userMp;

    /**
     * 用户EMAIL
     */
    private String userEmail;

    /**
     * 用户角色ID
     */
    private Long roleId;

    /**
     * 使用特殊权限(0:没有,1:有)
     */
    private boolean ubSpecPopedom;

    /**
     * 密保序号
     */
    private String passpodSn;

    /**
     * 动态密码
     */
    private String passpod;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserMp() {
        return userMp;
    }

    public void setUserMp(String userMp) {
        this.userMp = userMp;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public boolean getUbSpecPopedom() {
        return ubSpecPopedom;
    }

    public void setUbSpecPopedom(boolean ubSpecPopedom) {
        this.ubSpecPopedom = ubSpecPopedom;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getPasspodSn() {
        return passpodSn;
    }

    public void setPasspodSn(String passpodSn) {
        this.passpodSn = passpodSn;
    }

    public String getPasspod() {
        return passpod;
    }

    public void setPasspod(String passpod) {
        this.passpod = passpod;
    }

	public String getSdId() {
		return sdId;
	}

	public void setSdId(String sdId) {
		this.sdId = sdId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}


}
