package com.sdo.transbutton.common.exception;

import com.sdo.transbutton.common.utils.ThrowableUtils;

/**
 * 系统异常(使用者无法自行处理,需要开发人员干预的问题)
 * @description
 * @author Lincoln
 */
public class SystemException extends RuntimeException {
	
	private String[] args;

	/**
	 * 
	 */
	public SystemException() {
		super();

	}

	/**
	 * @param message
	 * @param cause
	 */
	public SystemException(String message, Throwable cause,String...args) {
		super(message, cause);
		this.args = args;

	}

	/**
	 * @param message
	 */
	public SystemException(String message,String...args) {
		super(message);
		this.args = args;
	}

	/**
	 * @param cause
	 */
	public SystemException(Throwable cause) {
		super(cause);

	}

	public String[] getArgs() {
		return args;
	}

	@Override
	public boolean equals(Object obj) {
		//判断空对象
		if(obj==null){
			return false;
		}
		
		//判断类型不一致的情况
		if(!(obj.getClass()==this.getClass())){
			return false;
		}
		
		return ThrowableUtils.getThrowableInfo(this).equals(ThrowableUtils.getThrowableInfo((Throwable) obj));
	}
	
	
}
