package com.shengpay.commons.hibernate.querybuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.transform.ResultTransformer;

import com.shengpay.commons.core.exception.SystemException;

public class BaseResultTransformer<Domain> implements ResultTransformer {

	private Class<Domain> cls;
	
	public BaseResultTransformer(Class<Domain> cls) {
		this.cls = cls;
	}

	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		return convert2class(tuple);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List<Domain> transformList(List collection) {
		if(collection==null) {
			return null;
		}
		List<Domain> domainList=new ArrayList<Domain>();
		for (Object object : collection) {
			domainList.add(convert2class(object));
		}
		return domainList;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Domain convert2class(Object obj) {
		if(obj==null) {
			return null;
		}
		
		if (cls.isInstance(obj)) {
			return (Domain) obj;
		}
		
		if(obj instanceof Map) {
			return (Domain) ((Map) obj).get(Criteria.ROOT_ALIAS);
		}

		if (!(obj instanceof Object[])) {
			throw new SystemException("无法将【" + obj + "】转换为【"+cls+"】");
		}

		Object[] objArr = (Object[]) obj;
		for (int i = 0; i < objArr.length; i++) {
			if (cls.isInstance(objArr[i])) {
				return (Domain) objArr[i];
			}
		}

		throw new SystemException("无法将【" + obj + "】转换为【"+cls+"】");
	}

}
