package com.shengpay.commons.hibernate.querybuilder;

import java.util.Date;
import java.util.List;

public interface BaseQueryBuilder<Domain> {

	List<Domain> getList();

	List<Domain> getList(Integer pageSize, Integer pageNO);

	int count();

	void setId(Long id);
	
	void setOrderByCreateTimeDesc(Boolean order);
	
	void setOrderByUpdateTimeDesc(Boolean order);
	
	void setOrderByCreateTimeAsc(Boolean order);
	
	void setOrderByUpdateTimeAsc(Boolean order);
	
	void setStatusVal(Integer status);
	
	void setStatusArr(Integer[] status);
	
	void setBeginCreateTime(Date date);
	
	void setEndCreateTime(Date date);
	
	void setBeginUpdateTime(Date date);
	
	void setEndUpdateTime(Date date);

	void setType(Integer type);

	void setIdArr(Long[] idArr);
	
	void setLoadAllDTO(Boolean loadAllDTO);

	void setExcludeStatusArr(Integer[] status);

	void setCacheTimeout(String cacheTimeout);

	public abstract void setReadOnly(Boolean readOnly);

	void setLock(Boolean lock);
}