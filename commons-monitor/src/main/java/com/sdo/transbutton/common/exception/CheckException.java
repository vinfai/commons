/**
 * 
 */
package com.sdo.transbutton.common.exception;

/**
 * 需要明确捕获的异常
 * @description 在于外部单位进行通讯过程中发生错误时抛出该异常
 * @author Lincoln
 */

public class CheckException extends Exception {

	public CheckException() {
		super();
	}

	public CheckException(String msg, Throwable t) {
		super(msg, t);
	}

	public CheckException(String msg) {
		super(msg);
	}

	public CheckException(Throwable t) {
		super(t);
	}

}
