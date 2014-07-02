package com.shengpay.commons.core.runinfo;

public interface RunInfoEmailSender {

	public abstract void sendEmail(String operationInfo, RunInfoCollector runInfoCollector);

}