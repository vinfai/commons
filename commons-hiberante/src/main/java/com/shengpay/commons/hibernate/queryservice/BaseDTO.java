package com.shengpay.commons.hibernate.queryservice;

import java.util.Date;

import com.shengpay.commons.core.base.BaseObject;
import com.shengpay.commons.core.base.PaginationRequest;
import com.shengpay.commons.core.exception.SystemException;
import com.shengpay.commons.core.utils.StatusUtils;

public class BaseDTO extends  BaseObject{
	public static final String CACHE_1_MINUTE   = "cache_1_minute";
	public static final String CACHE_3_MINUTE   = "cache_3_minute";
	public static final String CACHE_5_MINUTE   = "cache_5_minute";
	public static final String CACHE_10_MINUTE  = "cache_10_minute";
	public static final String CACHE_20_MINUTE  = "cache_20_minute";
	public static final String CACHE_30_MINUTE  = "cache_30_minute";
	public static final String CACHE_1_HOUR     = "cache_1_hour";
	public static final String CACHE_2_HOUR     = "cache_2_hour";
	public static final String CACHE_3_HOUR     = "cache_3_hour";
	public static final String CACHE_6_HOUR     = "cache_6_hour";
	public static final String CACHE_12_HOUR    = "cache_12_hour";
	public static final String CACHE_24_HOUR    = "cache_24_hour";

	private PaginationRequest page;
	
	private String cacheTimeout;

	private Boolean readOnly=Boolean.TRUE;
	
    /**
     * 主键
     */
    private Long id;
    
    private Boolean loadAllDTO;
    
    private Boolean lock;

	public Boolean getLock() {
		return lock;
	}

	public void setLock(Boolean lock) {
		this.lock = lock;
	}

	/**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 版本号
     */
    private Long version;
    
    private String name;
    
	private Boolean orderByCreateTimeDesc;
	
	private Boolean orderByUpdateTimeDesc;
	
	private Boolean orderByCreateTimeAsc;
	
	private Boolean orderByUpdateTimeAsc;
	
	private Integer statusVal;
	
	private Integer[] statusArr;
	private Long[] idArr;
	
	private Integer type;
	
	private Date beginCreateTime;
	private Date endCreateTime;
	
	private Date beginUpdateTime;
	private Date endUpdateTime;
	
	private Class<?> statusType;
	
	private Integer[] excludeStatusArr;

    public BaseDTO(Long id) {
		this(id,true);
	}

	public BaseDTO(Long id, boolean loadAllDTO) {
		if(id==null) {
			throw new SystemException("id不能为空！");
		}
		this.id = id;
		this.loadAllDTO = loadAllDTO;
	}

	public BaseDTO() {
	}

	/**
	 * @return the orderByCreateDesc
	 */
	public Boolean getOrderByCreateTimeDesc() {
		return orderByCreateTimeDesc;
	}

	/**
	 * @param orderByCreateDesc the orderByCreateDesc to set
	 */
	public void setOrderByCreateTimeDesc(Boolean orderByCreateDesc) {
		this.orderByCreateTimeDesc = orderByCreateDesc;
	}

	
	/**
	 * @return the page
	 */
	public PaginationRequest getPage() {
		return page;
	}
	

	/**
	 * @param page the page to set
	 */
	public void setPage(int pageSize,int pageNO) {
		this.page = new PaginationRequest(pageSize, pageNO);
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return the version
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

	/**
	 * @return the orderByUpdateTimeDesc
	 */
	public Boolean getOrderByUpdateTimeDesc() {
		return orderByUpdateTimeDesc;
	}

	/**
	 * @param orderByUpdateTimeDesc the orderByUpdateTimeDesc to set
	 */
	public void setOrderByUpdateTimeDesc(Boolean orderByUpdateTimeDesc) {
		this.orderByUpdateTimeDesc = orderByUpdateTimeDesc;
	}

	/**
	 * @return the orderByCreateTimeAsc
	 */
	public Boolean getOrderByCreateTimeAsc() {
		return orderByCreateTimeAsc;
	}

	/**
	 * @param orderByCreateTimeAsc the orderByCreateTimeAsc to set
	 */
	public void setOrderByCreateTimeAsc(Boolean orderByCreateTimeAsc) {
		this.orderByCreateTimeAsc = orderByCreateTimeAsc;
	}

	/**
	 * @return the orderByUpdateTimeAsc
	 */
	public Boolean getOrderByUpdateTimeAsc() {
		return orderByUpdateTimeAsc;
	}

	/**
	 * @param orderByUpdateTimeAsc the orderByUpdateTimeAsc to set
	 */
	public void setOrderByUpdateTimeAsc(Boolean orderByUpdateTimeAsc) {
		this.orderByUpdateTimeAsc = orderByUpdateTimeAsc;
	}

	/**
	 * @return the status
	 */
	public Integer getStatusVal() {
		return statusVal;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatusVal(Integer status) {
		this.statusVal = status;
	}

	/**
	 * @return the statusArr
	 */
	public Integer[] getStatusArr() {
		return statusArr;
	}

	/**
	 * @param statusArr the statusArr to set
	 */
	public void setStatusArr(Integer[] statusArr) {
		this.statusArr = statusArr;
	}

	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(PaginationRequest page) {
		this.page = page;
	}

    /**
     * @return the statusName
     */
    public String getStatusName() {
    	if(statusType==null || statusVal==null) {
    		return null;
    	}
    	
        return StatusUtils.getInstance().getName(statusType, statusVal);
    }

	/**
	 * @return the beginCreateTime
	 */
	public Date getBeginCreateTime() {
		return beginCreateTime;
	}

	/**
	 * @param beginCreateTime the beginCreateTime to set
	 */
	public void setBeginCreateTime(Date beginCreateTime) {
		this.beginCreateTime = beginCreateTime;
	}

	/**
	 * @return the endCreateTime
	 */
	public Date getEndCreateTime() {
		return endCreateTime;
	}

	/**
	 * @param endCreateTime the endCreateTime to set
	 */
	public void setEndCreateTime(Date endCreateTime) {
		this.endCreateTime = endCreateTime;
	}

	/**
	 * @return the beginUpdateTime
	 */
	public Date getBeginUpdateTime() {
		return beginUpdateTime;
	}

	/**
	 * @param beginUpdateTime the beginUpdateTime to set
	 */
	public void setBeginUpdateTime(Date beginUpdateTime) {
		this.beginUpdateTime = beginUpdateTime;
	}

	/**
	 * @return the endUpdateTime
	 */
	public Date getEndUpdateTime() {
		return endUpdateTime;
	}

	/**
	 * @param endUpdateTime the endUpdateTime to set
	 */
	public void setEndUpdateTime(Date endUpdateTime) {
		this.endUpdateTime = endUpdateTime;
	}

	/**
	 * @return the idArr
	 */
	public Long[] getIdArr() {
		return idArr;
	}

	/**
	 * @param idArr the idArr to set
	 */
	public void setIdArr(Long[] idArr) {
		this.idArr = idArr;
	}

	/**
	 * @return the statusType
	 */
	public Class<?> getStatusType() {
		return statusType;
	}

	/**
	 * @param statusType the statusType to set
	 */
	public void setStatusType(Class<?> statusType) {
		this.statusType = statusType;
	}

	/**
	 * @return the loadAllDTO
	 */
	public Boolean getLoadAllDTO() {
		return loadAllDTO;
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
	 * @return the excludeStatusArr
	 */
	public Integer[] getExcludeStatusArr() {
		return excludeStatusArr;
	}

	/**
	 * @param excludeStatusArr the excludeStatusArr to set
	 */
	public void setExcludeStatusArr(Integer[] excludeStatusArr) {
		this.excludeStatusArr = excludeStatusArr;
	}

	/**
	 * @return the cacheTimeout
	 */
	public String getCacheTimeout() {
		return cacheTimeout;
	}

	/**
	 * @param cacheTimeout the cacheTimeout to set
	 */
	public void setCacheTimeout(String cacheTimeout) {
		this.cacheTimeout = cacheTimeout;
	}

	/**
	 * @return the readOnly
	 */
	public Boolean getReadOnly() {
		return readOnly;
	}

	/**
	 * @param readOnly the readOnly to set
	 */
	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}


}
