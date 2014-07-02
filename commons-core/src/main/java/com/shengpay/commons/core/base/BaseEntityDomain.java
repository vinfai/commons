package com.shengpay.commons.core.base;

import java.util.Date;

import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.integralitycheck.IntegralityCheck;
import com.shengpay.commons.core.integralitycheck.IntegralityCheckable;
import com.shengpay.commons.core.utils.StatusUtils;

public class BaseEntityDomain<StatusType> extends BaseObject implements IntegralityCheckable {
	/**
	 * 交易流水号
	 */
	private Long id;

	/**
	 * 创建时间
	 */
	private Date createTime = new Date();

	/**
	 * 最后更新时间
	 */
	@UpdateTimeAtn
	private Date updateTime = new Date();

	/**
	 * 版本号
	 */
	private Long version = 0l;
	
	private Integer type;
	
	/**
	 * 实体名称
	 */
	private String name;

	public BaseEntityDomain(Long id) {
		super();
		this.id = id;
	}

	public BaseEntityDomain() {
		super();
	}

	public Long getId() {
		return id;
	}

	public Long getVersion() {
		return version;
	}

	public Date getCreateTime() {
		return (Date) createTime.clone();
	}

	public Date getUpdateTime() {
		return (Date) updateTime.clone();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if(obj==null) {
			return false;
		}
		
		if (this == obj)
			return true;
		
		if(!BaseEntityDomain.class.isInstance(obj)) {
			return false;
		}
		
		BaseEntityDomain other = (BaseEntityDomain) obj;
		Long otherId = other.getId();
		if (getId() == null && otherId==null) {
			return super.equals(obj);
		} 
		
		if(getId()==null || otherId==null) {
			return false;
		}
		
		return getId().equals(other.getId());
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setUpdateTime(Date lastUpdateTime) {
		this.updateTime = lastUpdateTime;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+"@{id=" + getId() + "}";
	}

	/**
	 * 对象完整性检查，在持久化前或重建后被调用，可以通过抛出BusinessException的方式预警
	 */
	@Override
	public void integralityCheck() {
		new IntegralityCheck(this).doCheck();
	}
	
	public StatusType getStatus() {
		return null;
	}
	
	public Integer getStatusVal() {
		if(getStatus()==null) {
			return null;
		}
		return StatusUtils.getInstance().getValue(getStatus());
	}

	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}
	
	public Class<StatusType> getStatusType(){
		return null;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		if(type==null) {
			throw new SystemException("类型不能为空！");
		}
		this.type = type;
	}
}
