package com.shengpay.commons.core.constants; 
/** 
 * 账户状态常量      
 * @usage		
 * @copyright	Copyright 2010 SDOCorporation. All rights reserved.
 * @company		SSDOorporation.
 * @author		zhangxiaochuan <zhangxc@SDSDOm>
 * @version     ConstantsStatusAccount.java,2010-1-5 zhangxiaochuan
 * @create		2010,2010-1-5
 */
public class ConstantsStatusAccount {

    /**
     * 账户类状态:初始状态（默认注册）
     */
    @ConstantTag(name = "初始", type = "账户类状态")
    public static final String ACCOUNT_STATUS_INIT="2000";

    /**
     * 账户类状态:激活
     */
    @ConstantTag(name = "激活", type = "账户类状态")
    public static final String ACCOUNT_STATUS_ACTIVE="2100";
    
    /**
     * 账户类状态:注销
     */
    @ConstantTag(name = "注销", type = "账户类状态")
    public static final String ACCOUNT_STATUS_SIGNOFF="2200";
    
    /**
     * 账户类状态:冻结
     */
    @ConstantTag(name = "冻结", type = "账户类状态")
    public static final String ACCOUNT_STATUS_LOCKED="2300";
}
 