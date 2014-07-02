package com.shengpay.commons.core.config;


import java.math.BigDecimal;
import java.util.List;


/**
 * @title 		配置管理服务
 * @description	
 * @usage		
 * @copyright	Copyright 2009 5173 Corporation. All rights reserved.
 * @company		5173 Corporation.
 * @author		wangmiaomiao <ex_wangmm@5173.com>
 * @version		$Id: ConfigService.java,v 1.2 2009-12-11 下午02:24:18 wangmiaomiao Exp $
 * @create		2009-12-11 下午02:24:18
 */
public interface ConfigService {

    /**
     * 取得配置信息
     * @param id 配置信息主键
     * @param params 配置信息的参数信息
     * @return
     */
    String getConfig(Long id, Object... params);

    /**
     * 读取boolean型的配置信息
     * @param id 配置信息主键
     * @param params 配置信息的参数信息
     * @return
     */
    Boolean getConfigByBoolean(Long id, Object... params);

    /**
     * 读取int型的配置信息
     * @param id 配置信息主键
     * @param params 配置信息的参数信息
     * @return
     */
    Integer getConfigByInteger(Long id, Object... params);

    /**
     * 读取double型的配置信息
     * @param id 配置信息主键
     * @param params 配置信息的参数信息
     * @return
     */
    Double getConfigByDouble(Long id, Object... params);

    /**
     * 读取Long型的配置信息
     * @param id 配置信息主键
     * @param params 配置信息的参数信息
     * @return
     */
    Long getConfigByLong(Long id, Object... params);
    
    /**
     * 将配置信息读取为一个区间配置信息vo
     * @param id
     * @return 如果配置信息不是区间型配置信息或者格式不符合要求，则抛出系统异常
     */
    LongRangeConfigVO getLongRangeConfigVO(Long id);
    
    /**
     * 获取double类型区间的配置信息
     * @param id
     * @return
     */
    BigDecimalRangeConfigVO getDoubleRangeConfigVO(Long id);
    
    /**
     * 读取字符串列表型的配置信息
     * @param id
     * @return
     */
    List<String> getConfigByStringList(Long id);
    
    /**
     * 读取long列表型的配置信息
     * @param id
     * @return
     */
    List<Long> getConfigByLongList(Long id);

	/**
	 * 取得BigDecimal类型的配置信息
	 * @param id
	 * @param params
	 * @return
	 */
	BigDecimal getConfigByBigDecimal(Long id, Object... params);
}
