package com.shengpay.commons.hibernate.queryservice;

import com.shengpay.commons.core.base.PaginationBaseObject;

public interface QueryService {
	
	<DTO extends BaseDTO> PaginationBaseObject<DTO> query(DTO templateDTO);

}
