/**
 * 
 */
package com.shengpay.commons.core.base;

import java.util.List;

/**
 * BaseDAO升级版
 * @author		LinDongCheng <lindongcheng@snda.com>
 * @version		2011-10-14 下午03:40:58
 */
public interface BaseDAO3<EntityEO> extends BaseDAO2<EntityEO>{
	
	void insertBatchHasId(List eoList);

	List<Object> insertBatch(List eoList);

	List<Integer> updateBatch(List eoList);
	
	List<Integer> updateBatchByField(List eoList);

	public abstract EntityEO select(Long id, boolean isLock, boolean noWait);
	
}
