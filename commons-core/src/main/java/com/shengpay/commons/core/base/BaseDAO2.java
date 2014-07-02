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
public interface BaseDAO2<EntityEO> extends BaseDAO<EntityEO>{

	
	/**
	 * 根据主键列表批量查询实体信息
	 * 
	 * @param idList 实体主键列表
	 * @param lockEO 是否锁定数据
	 * @return 查询到的实体对象（查询不到时返回空列表）
	 */
	List<EntityEO> selectListByIdList(List<Long> idList,boolean lockEO);
	
}
