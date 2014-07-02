/**
 * 
 */
package com.shengpay.commons.hibernate.queryservice;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;

import com.shengpay.commons.core.base.PaginationBaseObject;
import com.shengpay.commons.core.base.PaginationRequest;
import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.hibernate.propertiescopy.PropertiesCopy;
import com.shengpay.commons.hibernate.querybuilder.BaseQueryBuilder;

/**
 * 优惠活动服务
 * @author lindongcheng
 *
 */
public class QueryServiceImpl implements QueryService{
	
	@Resource
	private ApplicationContext ac;
	
	private Map<String,String> queryBuilderBeanNameMap=new HashMap<String, String>();

	/* (non-Javadoc)
	 * @see com.shengpay.qieke.qkcp.service.activepoi.ActivePoiQueryService#getList(com.shengpay.qieke.qkcp.service.activepoi.QueryActivePoiDTO)
	 */
	@SuppressWarnings("rawtypes")
	public <DTO extends BaseDTO> PaginationBaseObject<DTO> query(DTO queryDto){
		BaseQueryBuilder queryBuilder=makeQueryBuilder(queryDto);
		new PropertiesCopy(queryDto,queryBuilder,queryDto).copyProperties();
		
		PaginationBaseObject<DTO> pbo=new PaginationBaseObject<DTO>();
		setPageInfo(pbo, queryBuilder, queryDto);
		setData(queryDto, queryBuilder, pbo);
		return pbo;
	}
	
	@SuppressWarnings({ "rawtypes" })
	protected BaseQueryBuilder makeQueryBuilder(Object dto) {
		return (BaseQueryBuilder) ac.getBean(getQueryBuilderType(dto));
	}

	private String getQueryBuilderType(Object dto) {
		String name = dto.getClass().getName();
		String string = queryBuilderBeanNameMap.get(name);
		if(string==null) {
			throw new SystemException("请配置类型【"+name+"】对应的查询构建器Bean的名称！");
		}
		return string;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void setData(BaseDTO queryDTO, BaseQueryBuilder aqb, PaginationBaseObject pbo) {
		List list = getDataList(queryDTO, aqb);
		if(list==null || list.size()==0) {
			return;
		}
		List dtoList = PropertiesCopy.copyListByTemplate(list, queryDTO);
		pbo.setDataList(dtoList);
	}

	@SuppressWarnings("rawtypes")
	protected void setPageInfo(PaginationBaseObject pbo,BaseQueryBuilder aqb, BaseDTO queryDto) {
		PaginationRequest page = queryDto.getPage();
		if(page!=null) {
			int count=aqb.count();
			pbo.setPagination(count, page.getPageSize(), page.getPageNO());
		}
	}
	
	@SuppressWarnings("rawtypes")
	private List getDataList(BaseDTO queryDto,BaseQueryBuilder aqb){
		PaginationRequest page = queryDto.getPage();
		if(page!=null) {
			return aqb.getList(page.getPageSize(),page.getPageNO());
		}else {
			return aqb.getList();
		}
	}

	/**
	 * @param queryBuilderBeanClassMap the queryBuilderBeanClassMap to set
	 */
	public void setQueryBuilderBeanNameMap(Map<String, String> queryBuilderBeanClassMap) {
		this.queryBuilderBeanNameMap = queryBuilderBeanClassMap;
	}
}
