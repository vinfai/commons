package com.shengpay.commons.hibernate.base;

import java.util.List;

import com.shengpay.commons.core.base.BaseEntityDomain;



/**
 * 
 * @author lindongcheng
 *
 */
@SuppressWarnings("rawtypes")
public interface BaseRpt<Domain extends BaseEntityDomain>{

	Domain getById(Long id);
	
	<T> T getById(Class<T> cla,Long id);
	
	List<Domain> getList(Long[] ids);

	void saveOrUpdate(Object domain);
	
	void saveOrUpdateList(List<Domain> domain);
}