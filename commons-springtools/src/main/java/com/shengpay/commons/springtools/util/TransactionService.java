/*
 * File: TransactionService.java
 * ProjectName: CCR
 * Description: 
 * 
 * 
 * -----------------------------------------------------------
 * Date            Author          Changes
 * 2010-6-8         wuxin           created
 */
package com.shengpay.commons.springtools.util;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 功能: 封装事务操作
 * <p>
 * 用法:
 * 
 * @version 1.0
 */

public class TransactionService {
	
	private PlatformTransactionManager transactionManager;

	//private TransactionStatus status;
	
	public void setTransactionManager(
			PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	/**
	 * 开始事务
	 */
	public TransactionStatus begin(){
		TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		//this.status = transactionManager.getTransaction(definition);
		return transactionManager.getTransaction(definition);
	}
	
	/**
	 * 开始一个新的事务
	 */
	public TransactionStatus beginNew(){
		TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		//this.status = transactionManager.getTransaction(definition);
		return transactionManager.getTransaction(definition);
	}
	
	/**
	 * 提交事务
	 */
	public void commit(TransactionStatus status){
		transactionManager.commit(status);
	}
	/**
	 * 回滚事务
	 */
	public void rollback(TransactionStatus status){
		transactionManager.rollback(status);
	}

}
