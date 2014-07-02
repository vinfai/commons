/**
 * 
 */
package com.shengpay.commons.hibernate.querybuilder;

import static org.hibernate.criterion.Order.asc;
import static org.hibernate.criterion.Order.desc;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.ge;
import static org.hibernate.criterion.Restrictions.in;
import static org.hibernate.criterion.Restrictions.le;
import static org.hibernate.criterion.Restrictions.not;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.shengpay.commons.core.utils.ClassUtils;
import com.shengpay.commons.core.utils.StatusUtils;

/**
 * 优惠活动查询器构建实现
 * 
 * @author lindongcheng
 * 
 */
public class BaseQueryBuilderImpl<Domain> implements BaseQueryBuilder<Domain> {

	private HibernateTemplate hibernateTemplate;

	private DetachedCriteria criteria;

	@SuppressWarnings("rawtypes")
	private Class domainClass;
	
	private BaseResultTransformer<Domain> resultTransformer;

	public BaseQueryBuilderImpl() {
		domainClass = ClassUtils.getGenericType(getClass(), 0);
	}

	@SuppressWarnings("unchecked")
	public DetachedCriteria getCriteria() {
		if (criteria == null) {
			resultTransformer = new BaseResultTransformer<Domain>(domainClass);
			criteria = DetachedCriteria.forClass(domainClass);
		}
		return criteria;
	}

	/*
	 * 
	 * @see com.shengpay.qieke.qkcp.rpt.active.BaseQueryBuilder#getList()
	 */
	@Override
	public List<Domain> getList() {
		return getList(null, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.shengpay.qieke.qkcp.rpt.active.BaseQueryBuilder#getList(int,
	 * int)
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public List<Domain> getList(Integer pageSize, Integer pageNO) {
		getCriteria().setProjection(null);
		getCriteria().setResultTransformer(resultTransformer);
		if (pageSize != null && pageNO != null) {
			return getHibernateTemplate().findByCriteria(getCriteria(), (pageNO - 1) * pageSize, pageSize);
		}
		return getHibernateTemplate().findByCriteria(getCriteria());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.shengpay.qieke.qkcp.rpt.active.BaseQueryBuilder#count()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public int count() {
		getCriteria().setProjection(Projections.count("id"));
		List<Domain> list = getHibernateTemplate().findByCriteria(criteria);
		if (list == null || list.size() == 0) {
			return 0;
		}
		return ((Long) list.get(0)).intValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.shengpay.qieke.qkcp.rpt.base.BaseQueryBuilder#setId(java.lang.Long)
	 */
	@Override
	public void setId(Long id) {
		if (id == null) {
			return;
		}

		getCriteria().add(eq("id", id));
	}

	@Override
	public void setOrderByCreateTimeDesc(Boolean order) {
		if (order == null) {
			return;
		}

		getCriteria().addOrder(desc("createTime"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.shengpay.qieke.qkcp.rpt.base.BaseQueryBuilder#setOrderByCreateTimeAsc
	 * (java.lang.Boolean)
	 */
	@Override
	public void setOrderByCreateTimeAsc(Boolean order) {
		if (order == null) {
			return;
		}

		getCriteria().addOrder(asc("createTime"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.shengpay.qieke.qkcp.rpt.base.BaseQueryBuilder#setOrderByUpdateTimeDesc
	 * (java.lang.Boolean)
	 */
	@Override
	public void setOrderByUpdateTimeDesc(Boolean order) {
		if (order == null) {
			return;
		}

		getCriteria().addOrder(desc("updateTime"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.shengpay.qieke.qkcp.rpt.base.BaseQueryBuilder#setOrderByUpdateTimeAsc
	 * (java.lang.Boolean)
	 */
	@Override
	public void setOrderByUpdateTimeAsc(Boolean order) {
		if (order == null) {
			return;
		}

		getCriteria().addOrder(asc("updateTime"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.shengpay.qieke.qkcp.rpt.base.BaseQueryBuilder#setStatus(java.lang
	 * .Integer)
	 */
	@Override
	public void setStatusVal(Integer status) {
		if (status == null || getStatusClass() == null) {
			return;
		}

		getCriteria().add(eq("status", StatusUtils.getInstance().getStatus(getStatusClass(), status)));
	}

	protected Class<Object> getStatusClass() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.shengpay.qieke.qkcp.rpt.base.BaseQueryBuilder#setStatus(java.lang
	 * .Integer[])
	 */
	@Override
	public void setStatusArr(Integer[] status) {
		if (status == null || status.length == 0 || getStatusClass() == null) {
			return;
		}

		getCriteria().add(in("status", StatusUtils.getInstance().getStatus(getStatusClass(), status)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.shengpay.qieke.qkcp.rpt.base.BaseQueryBuilder#setStatus(java.lang
	 * .Integer[])
	 */
	@Override
	public void setExcludeStatusArr(Integer[] status) {
		if (status == null || status.length == 0 || getStatusClass() == null) {
			return;
		}

		getCriteria().add(not(in("status", StatusUtils.getInstance().getStatus(getStatusClass(), status))));
	}

	private void setBeginTime(String propertyName, Date activeBeginTime) {
		if (activeBeginTime == null) {
			return;
		}

		getCriteria().add(ge(propertyName, activeBeginTime));
	}

	private void setEndTime(String propertyName, Date activeEndTime) {
		if (activeEndTime == null) {
			return;
		}

		getCriteria().add(le(propertyName, activeEndTime));
	}

	@Override
	public void setBeginCreateTime(Date date) {
		setBeginTime("createTime", date);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.shengpay.qieke.qkcp.rpt.base.BaseQueryBuilder#setEndCreateTime(java
	 * .util.Date)
	 */
	@Override
	public void setEndCreateTime(Date date) {
		setEndTime("createTime", date);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.shengpay.qieke.qkcp.rpt.base.BaseQueryBuilder#setBeginUpdateTime(
	 * java.util.Date)
	 */
	@Override
	public void setBeginUpdateTime(Date date) {
		setBeginTime("updateTime", date);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.shengpay.qieke.qkcp.rpt.base.BaseQueryBuilder#setEndUpdateTime(java
	 * .util.Date)
	 */
	@Override
	public void setEndUpdateTime(Date date) {
		setEndTime("updateTime", date);
	}

	@Override
	public void setType(Integer type) {
		if (type == null) {
			return;
		}
		getCriteria().add(eq("type", type));
	}

	@Override
	public void setLock(Boolean lock) {
		if (lock != null && lock) {
			getCriteria().setLockMode(LockMode.PESSIMISTIC_WRITE);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.shengpay.qieke.qkcp.rpt.base.BaseQueryBuilder#setIdArr(java.lang.
	 * Long[])
	 */
	@Override
	public void setIdArr(Long[] idArr) {
		if (idArr == null || idArr.length == 0) {
			return;
		}

		getCriteria().add(in("id", idArr));
	}

	@Override
	public void setLoadAllDTO(Boolean loadAllDTO) {
		if (!loadAllDTO) {
			return;
		}

		loadAllDTO();
	}

	protected void loadAllDTO() {
	}

	protected HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	protected void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	@Override
	public void setCacheTimeout(String cacheTimeout) {
		// getCriteria().setCacheMode(CacheMode.NORMAL);
		// getCriteria().setCacheable(true);
		// getCriteria().setCacheRegion(cacheTimeout);
	}

	@Override
	public void setReadOnly(Boolean readOnly) {
		// getCriteria().setReadOnly(readOnly);
	}
}
