/*
 * Copyright 2013 shengpay.com, Inc. All rights reserved.
 * shengpay.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : kuguobing
 * create time : 2013-8-1 下午2:44:29
 */
package com.shengpay.commons.bp.pipe.support;

import com.shengpay.commons.bp.pipe.PipelineHandlerChain;
import com.shengpay.commons.bp.pipe.HandlerException;

/**
 * 功能描述：
 * @author kuguobing
 * time : 2013-8-1 下午2:44:29
 */
public class NoopHandlerChain<I, O> implements PipelineHandlerChain<I, O> {

    @Override
    public void doHandle(I request, O response) throws HandlerException {
        //DO nothing
    }
}
