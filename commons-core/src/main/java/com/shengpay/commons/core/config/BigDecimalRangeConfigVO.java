/**
 * 
 */
package com.shengpay.commons.core.config;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.utils.StringUtils;


/**
 * @title 		double类型区间配置信息
 * @description	
 * @usage		
 * @copyright	Copyright 2009 5173 Corporation. All rights reserved.
 * @company		5173 Corporation.
 * @author		wangmiaomiao <ex_wangmm@5173.com>
 * @version		$Id: DoubleRangeConfigVO.java,v 1.2 2009-12-29 下午05:12:35 wangmiaomiao Exp $
 * @create		2009-12-29 下午05:12:35
 */
public class BigDecimalRangeConfigVO {
    /**
     * 区间格式正则
     */
    public static final Pattern DOUBLE_RANGE_PATTERN = Pattern.compile("^(\\(|\\[)((?:\\d+\\.)?\\d+)\\~((?:\\d+\\.)?\\d+)(\\)|\\])$");
    
    /**
     * 最小值
     */
    private BigDecimal min;
    
    /**
     * 最大值
     */
    private BigDecimal max;
    
    /**
     * 左侧是否开区间
     */
    public boolean includeMin;

    /**
     * 右侧是否开区间
     */
    public boolean includeMax;
    
    public BigDecimalRangeConfigVO(String configValue) {
    	//验证参数合法性
    	if(StringUtils.isBlank(configValue)){
    		throw new SystemException("参数不合法:BigDecimalRangeConfigVO(String "+configValue+")");
    	}
    	
    	// 不为空且符合规则
        Matcher matcher=DOUBLE_RANGE_PATTERN.matcher(configValue);
        if (!matcher.find() || matcher.groupCount()!=4) {
        	throw new SystemException("字符串【"+configValue+"】不符合数字区间配置格式！");
        }
        
        includeMin = "[".equals(matcher.group(1)); 
        min = new BigDecimal(matcher.group(2));
        max = new BigDecimal(matcher.group(3));
        includeMax = "]".equals(matcher.group(4)); 
    }
    
    public boolean isInRange(double target) {
    	return isInRange(new BigDecimal(target));
    }
    
    public boolean isInRange(BigDecimal target) {
    	if(target==null){
    		return false;
    	}

    	return isInMin(target)&&isInMax(target);
    }
    
    /**
     * 
     * @param target
     * @return
     */
    private boolean isInMin(BigDecimal target){
    	if(includeMin){
    		return min.compareTo(target)<=0;
    	}else{
    		return min.compareTo(target)<0;
    	}
    }

    private boolean isInMax(BigDecimal target){
    	if(includeMax){
    		return max.compareTo(target)>=0;
    	}else{
    		return max.compareTo(target)>0;
    	}
    }
    
    /**
     * 获取“left”(类型：double)
     */
    public double getMin() {
        return min.doubleValue();
    }

    /**
     * 获取“right”(类型：double)
     */
    public double getMax() {
        return max.doubleValue();
    }

    /**
     * 获取“includeLeft”(类型：boolean)
     */
    public boolean isIncludeMin() {
        return includeMin;
    }

    /**
     * 获取“includeRight”(类型：boolean)
     */
    public boolean isIncludeMax() {
        return includeMax;
    }
}
