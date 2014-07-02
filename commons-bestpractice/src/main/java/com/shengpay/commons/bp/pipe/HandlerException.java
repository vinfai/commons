package com.shengpay.commons.bp.pipe;

import com.shengpay.commons.core.exception.BusinessException;

/**
 * 
 * 功能描述：Pipeline Handler处理器异常
 * @author kuguobing
 * time : 2012-10-26 上午11:49:55
 */
public class HandlerException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public HandlerException(Level level, String errorCode, Object... theArgs) {
        super(level, errorCode, theArgs);
    }

    public HandlerException(String errorCode, Object... theArgs) {
        super(errorCode, theArgs);
    }

}
