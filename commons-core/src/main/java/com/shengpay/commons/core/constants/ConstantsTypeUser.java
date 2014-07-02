/**
 * 
 */
package com.shengpay.commons.core.constants;

/**
 * * @description	
 * @usage		
 * @copyright	Copyright 2010 SDO Corporation. All rights reserved.
 * @company		SDOCorporation.
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		$Id: ConstantsTypeUser.java,v 1.0 2010-3-22 下午05:16:28 lindc Exp $
 * @create		2010-3-22 下午05:16:28
 */

public interface ConstantsTypeUser {
	/**
	 * 用户类类型:系统管理员
	 */
    @ConstantTag(name="系统管理员", type="用户类类型")
	public static final String UB_TYPE_ADMIN = "admi";    
    
	
	/**
	 * 用户类类型:普通用户
	 */
    @ConstantTag(name="普通用户", type="用户类类型")
	public static final String UB_TYPE_CLIENT = "clie";    
    
    /**
     * 用户类类型:银行用户
     */
    @ConstantTag(name="网上银行", type="用户类类型")
    public static final String UB_TYPE_BANK_ONLINE= "bkon";  
    
    /**
     * 用户类类型:银行用户
     */
    @ConstantTag(name="汇款银行", type="用户类类型")
    public static final String UB_TYPE_BANK_REMIT = "bkof";  
    
    /**
     * 用户类类型:银行用户
     */
    @ConstantTag(name="提现银行", type="用户类类型")
    public static final String UB_TYPE_BANK_CASH = "bkch";  
    
    /**
     * 用户类类型:银行用户
     */
    @ConstantTag(name="系统用户", type="用户类类型")
    public static final String UB_TYPE_SYSTEM = "syst";    
    
    /**
     * 用户类类型:C2C商户
     */
    @ConstantTag(name="C2C商户", type="用户类类型")
    public static final String UB_TYPE_MERCHANT_C2C = "mc2c";
    
    /**
     * 用户类类型:B2C商户
     */
    @ConstantTag(name="B2C商户", type="用户类类型")
    public static final String UB_TYPE_MERCHANT_B2C = "mb2c";
    
    
    

}
