/*
 * Copyright 2013 shengpay.com, Inc. All rights reserved.
 * shengpay.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : kuguobing
 * create time : 2013-9-13 上午8:52:10
 */
package com.shengpay.commons.bp.featuretoggle;

/**
 * 功能描述：特性功能开关回调接口定义
 * @author kuguobing
 * time : 2013-9-13 上午8:52:10
 */
public interface FeatureToggleCallback {

    /**
     * 动态获取开关状态 - 开关参数配置在参数文件（比如启动加载）或数据库中（缓存或动态获取）
     * 【true：代表有开关且开关状态打开  false： 代表没有开关或开关状态已关闭】
     * @return
     */
    boolean resolveToggle();
}
