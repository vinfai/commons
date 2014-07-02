/**
 * 
 */
package com.shengpay.commons.core.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.shengpay.commons.core.exception.SystemException;


/**
 * @title 		整形区间配置信息
 * @description	
 * @usage		
 * @copyright	Copyright 2009 5173 Corporation. All rights reserved.
 * @company		5173 Corporation.
 * @author		wangmiaomiao <ex_wangmm@5173.com>
 * @version		$Id: LongRangeConfigVO.java,v 1.2 2009-12-29 下午05:08:42 wangmiaomiao Exp $
 * @create		2009-12-29 下午05:08:42
 */
public class LongRangeConfigVO {
    
    /**
     * 区间格式正则
     */
    public static final Pattern LONG_RANGE_PATTERN = Pattern.compile("^(\\(|\\[)(\\d+)\\~(\\d+)(\\)|\\])$");
    
    private Long left;
    
    private Long right;
    
    public LongRangeConfigVO(String configValue) {
        Matcher matcher;
        // 不为空且符合规则
        if (configValue != null && (matcher = LONG_RANGE_PATTERN.matcher(configValue)).find()) {
            boolean includeLeft = "[".equals(matcher.group(1)); // 左侧是否开区间
            this.left = Long.parseLong(matcher.group(2)); // 左侧值
            this.right = Long.parseLong(matcher.group(3)); // 右侧值
            boolean includeRight = "]".equals(matcher.group(4)); // 右侧是否开区间
            
            if (includeLeft) {
                this.left -= 1;
            }
            
            if (includeRight) {
                this.right += 1;
            }
        } else {
            throw new SystemException(String.format("格式不符合规范，请确认【%s】", configValue));
        }        
    }

    
    public boolean isInRange(long target) {
        return this.left < target && this.right > target;
    }
}
