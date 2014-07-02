/*
 * Copyright 2013 shengpay.com, Inc. All rights reserved.
 * shengpay.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : kuguobing
 * create time : 2013-8-1 下午2:29:00
 */
package com.shengpay.commons.bp.pipe;

/**
 * 功能描述：抽象的Pipehandler处理
 * @author kuguobing
 * time : 2013-8-1 下午2:29:00
 */
public abstract class AbsPipelineHandler<I, O> implements PipelineHandler<I, O> {

    /**
     * Handler处理器名称
     */
    private String name;

    /**
     * Handler 描述
     */
    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
