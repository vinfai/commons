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
public class ConstantsStatusSwitch {

    /**
     * 开关状态:打开/有效
     */
    @ConstantTag(name = "打开/有效", type = "开关状态")
    public static final String ACCOUNT_STATUS_SWITCH_ON="1000";

    /**
     * 开关状态:关闭/无效
     */
    @ConstantTag(name = "关闭/无效", type = "开关状态")
    public static final String ACCOUNT_STATUS_SWITCH_OFF="2000";
}
 