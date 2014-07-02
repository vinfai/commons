package com.sdo.transbutton.common.exception;

/**
 * 业务异常
 * @description 当业务执行过程中,需要提示某些业务信息时抛出该异常
 * @author Lincoln
 */
public class BusinessException extends Exception {

	/**
	 * 业务信息编码
	 */
	private String businessCode;

	/**
	 * 业务信息参数
	 */
	private final Object[] args;

	/**
	 * @param errorCode
	 * @param args
	 */
	public BusinessException(String errorCode, Object... theArgs) {
		super(errorCode);
		this.businessCode = errorCode;
		this.args = theArgs;
	}

	/**
	 * 获取“args”(类型：Object[])
	 */
	public Object[] getArgs() {
		return args;
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#toString()
	 */
	@Override
	public String toString() {
		StringBuffer argsBuf = new StringBuffer();
		for (int i = 0; args != null && i < args.length; i++) {
			argsBuf.append(args[i]);
			if (i + 1 < args.length) {
				argsBuf.append(",");
			}
		}

		return "{" + getClass().getName() + "@" + hashCode() + "[" + businessCode + "(" + argsBuf + ")]}";
	}

	/**
	 * @return the busiInfoCode
	 */
	public String getBusinessCode() {
		return businessCode;
	}

	/**
	 * @param busiInfoCode the busiInfoCode to set
	 */
	public void setBusinessCode(String busiInfoCode) {
		this.businessCode = busiInfoCode;
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
		
		//判断业务码不相同情况
		BusinessException otherBe=(BusinessException) obj;
		if(!otherBe.businessCode.equals(businessCode)){
			return false;
		}
		
		//判断参数数量不同的情况
		if(otherBe.args.length!=args.length){
			return false;
		}
		
		//判断参数内容不相同情况
		for (int i = 0; i < args.length; i++) {
			if(!args[i].equals(otherBe.args[i])){
				return false;
			}
		}
		
		return true;
	}
}
