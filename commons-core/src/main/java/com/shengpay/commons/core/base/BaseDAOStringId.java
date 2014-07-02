package com.shengpay.commons.core.base;

import java.util.List;


/**
 *  数据访问对象基础接口
 * @description
 * @author Lincoln
 */
public interface BaseDAOStringId<EntityEO> {

	/**
	 * 将实体对象持久化；
	 * 
	 * @param to 被持久化TO
	 * @return 实体对象的主键
	 */
	String insert(EntityEO eo);

	/**
	 * 查询指定主键的持久化对象；
	 * 
	 * @param id 实体对象的主键；
	 * @return 查询到的实体对象（查询不到时返回null）
	 */
	EntityEO select(String id);

	/**
	 * 查询并锁定记录
	 * @param id
	 * @return
	 */
	EntityEO selectAndLock(String id);

	/**
	 * 根据指定EO的当前信息从数据库查询对应EO对象
	 * 
	 * @param id 实体对象的主键；
	 * @return 查询到的实体对象（查询不到时返回null）
	 */
	EntityEO selectEOByEO(EntityEO eo);
	
	/**
	 * 根据指定EO的当前信息从数据库查询对应EO对象
	 * 
	 * @param id 实体对象的主键；
	 * @return 查询到的实体对象（查询不到时返回null）
	 */
	List<EntityEO> selectListByEO(EntityEO eo);
	
	/**
	 * 根据指定EO的当前信息从数据库查询对应EO对象
	 * @param id 实体对象的主键；
	 * 
	 * @return 查询到的实体对象（查询不到时返回null）
	 */
	List<EntityEO> selectListByEO(EntityEO eo,int pageNO,int pageSize);
	
	/**
	 * 根据指定EO的当前信息从数据库查询对应EO对象
	 * 
	 * @param id 实体对象的主键；
	 * @return 查询到的实体对象（查询不到时返回null）
	 */
	int countByEO(EntityEO eo);

	/**
	 * 更新持久化对象；
	 * 
	 * @param eo 被更新的实体对象；
	 * @return 被更新的实体对象的数量；
	 */
	int update(EntityEO eo);
	
	/**
	 * 更新持久化对象；更新指定字段（注：版本号，主键需设置）
	 * @param eo 被更新的实体对象；
	 * @return 被更新的实体对象的数量；
	 */
	int updateByField(EntityEO eo);
	
	/**
	 * 删除指定对象
	 * @param eo
	 * @return
	 */
	int delete(EntityEO eo);
	
	/**
	 * 根据主键列表批量查询实体信息
	 * 
	 * @param idList 实体主键列表
	 * @param lockEO 是否锁定数据
	 * @return 查询到的实体对象（查询不到时返回空列表）
	 */
	List<EntityEO> selectListByIdList(List<String> idList,boolean lockEO);
}
