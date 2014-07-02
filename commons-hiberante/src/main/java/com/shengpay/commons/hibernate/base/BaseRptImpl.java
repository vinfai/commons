/**
 * 
 */
package com.shengpay.commons.hibernate.base;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.shengpay.commons.core.base.BaseEntityDomain;
import com.shengpay.commons.core.exception.BusinessException;
import com.shengpay.commons.core.utils.ClassUtils;

/**
 * 
 * @author lindongcheng
 * 
 */
@Service
@SuppressWarnings("rawtypes")
public class BaseRptImpl<Domain extends BaseEntityDomain> implements BaseRpt<Domain> {

	private HibernateTemplate hibernateTemplate;

	private Class domainClass;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.shengpay.qieke.qkcp.member.MemberRpt#getById(java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Domain getById(Long id) {
		if (id == null) {
			return null;
		}

		return (Domain) hibernateTemplate.get(getDomainClass(), id);
	}

	@Override
	public void saveOrUpdate(Object domain) {
		hibernateTemplate.save(domain);
	}

	private Class getDomainClass() {
		if (domainClass == null) {
			domainClass = ClassUtils.getGenericType(getClass(), 0);
		}
		return domainClass;
	}

	/**
	 * @return the hibernateTemplate
	 */
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	/**
	 * @param hibernateTemplate
	 *            the hibernateTemplate to set
	 */
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	@Override
	public void saveOrUpdateList(List<Domain> list) {
		if(list==null || list.size()==0) {
			return;
		}
		
		for (Domain domain : list) {
			saveOrUpdate(domain);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Domain> getList(final Long[] ids) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery("from "+getDomainClassName()+" where id in (:ids)");
				query.setParameterList("ids", ids);
				return query.list();
			}
		});
	}

	protected String getDomainClassName() {
		return getDomainClass().getName();
	}

	@Override
	public <T> T getById(Class<T> cla, Long id) {
		return hibernateTemplate.get(cla, id);
	}
}
