/*
 * Copyright 2013 shengpay.com, Inc. All rights reserved.
 * shengpay.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : kuguobing
 * create time : 2013-9-13 上午8:49:59
 */
package com.shengpay.commons.bp.featuretoggle;

/**
 * 功能描述：特性功能开关Template模板类
 * @author kuguobing
 * time : 2013-9-13 上午8:49:59
 */
public class FeatureToggleTemplate<T> {

    private T onTarget;

    private T offTarget;

    protected T getOnTarget() {
        return onTarget;
    }

    public void setOnTarget(T onTarget) {
        this.onTarget = onTarget;
    }

    protected T getOffTarget() {
        return offTarget;
    }

    public void setOffTarget(T offTarget) {
        this.offTarget = offTarget;
    }

}
