/*
 * Copyright 2012 shengpay.com, Inc. All rights reserved.
 * shengpay.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : kuguobing
 * create time : 2012-12-10 下午12:16:53
 */
package com.shengpay.commons.bp.pipe.group;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

import com.shengpay.commons.bp.pipe.AbsPipelineHandler;
import com.shengpay.commons.bp.pipe.PipelineHandlerChain;
import com.shengpay.commons.bp.pipe.HandlerException;
import com.shengpay.commons.bp.pipe.PipelineHandler;
import com.shengpay.commons.bp.pipe.support.ListHandlerChain;
import com.shengpay.commons.bp.pipe.support.NoopHandlerChain;

/**
 * 功能描述：分组Handler执行器，供分组子类继承实现相应的逻辑
 * @author kuguobing
 * time : 2012-12-10 下午12:16:53
 */
public abstract class GroupPipelineHandler<I, O> extends AbsPipelineHandler<I, O> {

    /**
     * 处理器列表
     */
    private List<PipelineHandler<I, O>> handlers;

    /**
     * 是否是异步运行模式【多个Handler异步执行-->Future同步取最终返回结果】
     */
    private boolean asyncMode = false;

    /**
     * 执行的线程池
     */
    private ExecutorService executorService = null;

    public void setAsyncMode(boolean asyncMode) {
        this.asyncMode = asyncMode;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * 设置分组的Handler列表
     * @param handlers
     */
    public void setHandlers(List<PipelineHandler<I, O>> handlers) {
        if (handlers != null)
            this.handlers = Collections.unmodifiableList(handlers); //ImmutableList.copyOf(handlers);
    }

    protected List<PipelineHandler<I, O>> getHandlers() {
        return handlers;
    }

    @Override
    public void handle(final I request, final O response, final PipelineHandlerChain<I, O> handlerChain)
            throws HandlerException {
        if (handlers == null){
            handlerChain.doHandle(request, response);
			return;
		}

        if (asyncMode && executorService != null) {
            // 异步Handler处理链
            final PipelineHandlerChain<I, O> noopHandlerChain = new NoopHandlerChain<I, O>();
            for (final PipelineHandler<I, O> handler : handlers) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        handler.handle(request, response, noopHandlerChain);
                    }
                });
            }

        } else {
            //同步Handler处理链
            PipelineHandlerChain<I, O> innerSubHandlerChain = new ListHandlerChain<I, O>(handlers);
            innerSubHandlerChain.doHandle(request, response);
        }

        //继续执行外部Hhandler chain itself
        handlerChain.doHandle(request, response);
    }
}
