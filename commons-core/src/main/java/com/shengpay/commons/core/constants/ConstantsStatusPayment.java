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
public class ConstantsStatusPayment {
    
    @ConstantTag(name = "待收款", type = "PAYMENT_STATUS")
    public static final String PAYMENT_STATUS_GATHER_WAIT = "1000";
    
    @ConstantTag(name = "收款成功", type = "PAYMENT_STATUS")
    public static final String PAYMENT_STATUS_GATHER_SUCC = "1101";
    
    @ConstantTag(name = "收款失败", type = "PAYMENT_STATUS")
    public static final String PAYMENT_STATUS_GATHER_FAIL= "1201";
    
    public static final String PAYMENT_STATUS_GATHER_REFUND= "1202";
    
    @ConstantTag(name = "确认收款成功", type = "PAYMENT_STATUS")
    public static final String PAYMENT_STATUS_GATHER_SUCC_CONFIM = "1102";
    
    @ConstantTag(name = "收款结果未知", type = "PAYMENT_STATUS")
    public static final String PAYMENT_STATUS_GATHER_UNKNOW = "1103";
    
    @ConstantTag(name = "付款成功", type = "PAYMENT_STATUS")
    public static final String PAYMENT_STATUS_PAYMENT_SUCC = "2101";
    
    @ConstantTag(name = "付款失败", type = "PAYMENT_STATUS")
    public static final String PAYMENT_STATUS_PAYMENT_FAIL = "2201";
    
    @ConstantTag(name = "付款结果未知", type = "PAYMENT_STATUS")
    public static final String PAYMENT_STATUS_PAYMENT_UNKNOW = "2301";
    
    @ConstantTag(name = "退款中", type = "PAYMENT_STATUS")
    public static final String PAYMENT_STATUS_REFUNDING = "3001";
    
    @ConstantTag(name = "退款成功", type = "PAYMENT_STATUS")
    public static final String PAYMENT_STATUS_REFUND_SUCC = "3101";
    
    @ConstantTag(name = "退款失败", type = "PAYMENT_STATUS")
    public static final String PAYMENT_STATUS_REFUND_FAIL = "3201";
    
    @ConstantTag(name = "退款结果未知", type = "PAYMENT_STATUS")
    public static final String PAYMENT_STATUS_REFUND_UNKNOW = "3301";
}
 