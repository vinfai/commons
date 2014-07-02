/*
 * Copyright 2013 shengpay.com, Inc. All rights reserved.
 * shengpay.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : kuguobing
 * create time : 2013-9-9 上午9:56:37
 */
package com.shengpay.commons.bp.pipe.support;

import com.shengpay.commons.bp.pipe.HandlerException;
import com.shengpay.commons.bp.pipe.PipelineHandler;
import com.shengpay.commons.bp.pipe.PipelineHandlerChain;

/**
 * 功能描述：过滤链处理器Adapter适配器
 * @author kuguobing
 * time : 2013-9-9 上午9:56:37
 */
public class PipelineHandlerAdapter<I, O> implements PipelineHandler<I, O> {

    private PipelineHandler<I, O> adapteeHandler;

    protected PipelineHandler<I, O> getAdapteeHandler() {
        return adapteeHandler;
    }

    public void setAdapteeHandler(PipelineHandler<I, O> adapteeHandler) {
        this.adapteeHandler = adapteeHandler;
    }

    @Override
    public void handle(I request, O response, PipelineHandlerChain<I, O> handlerChain) throws HandlerException {
        this.adapteeHandler.handle(request, response, handlerChain);
    }

}
