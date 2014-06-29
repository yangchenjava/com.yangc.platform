package com.yangc.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.yangc.bean.BaseBean;

@SuppressWarnings("rawtypes")
public interface BaseDao {

	/**
	 * @功能: 保存实体
	 * @作者: yangc
	 * @创建日期: 2013-7-29 下午12:05:09
	 * @param bean
	 * @return
	 */
	public Serializable save(BaseBean bean);

	/**
	 * @功能: 保存或更新实体
	 * @作者: yangc
	 * @创建日期: 2013-7-29 下午12:06:25
	 * @param bean
	 */
	public void saveOrUpdate(BaseBean bean);

	/**
	 * @功能: 批量保存或更新列表
	 * @作者: yangc
	 * @创建日期: 2013-7-29 下午12:06:21
	 * @param list
	 */
	public void saveOrUpdate(List<BaseBean> list);

	/**
	 * @功能: 删除
	 * @作者: yangc
	 * @创建日期: 2013-7-29 下午12:06:16
	 * @param clazz
	 * @param id
	 */
	public void delete(Class<? extends BaseBean> clazz, Serializable id);

	/**
	 * @功能: 更新实体
	 * @作者: yangc
	 * @创建日期: 2013-7-29 下午12:06:12
	 * @param bean
	 */
	public void update(BaseBean bean);

	/**
	 * @功能: 更新或删除
	 * @作者: yangc
	 * @创建日期: 2013-7-29 下午12:06:07
	 * @param hql
	 * @param values
	 */
	public void updateOrDelete(String hql, Object[] values);

	/**
	 * @功能: 查询一条
	 * @作者: yangc
	 * @创建日期: 2013-7-29 下午12:06:02
	 * @param clazz
	 * @param id
	 * @return
	 */
	public BaseBean get(Class<? extends BaseBean> clazz, Serializable id);

	/**
	 * @功能: 查询一条
	 * @作者: yangc
	 * @创建日期: 2013-7-29 下午12:05:57
	 * @param className
	 * @param id
	 * @return
	 */
	public BaseBean get(String className, Serializable id);

	/**
	 * @功能: 查询一条
	 * @作者: yangc
	 * @创建日期: 2013-7-29 下午12:05:39
	 * @param hql
	 * @param values
	 * @return
	 */
	public BaseBean get(String hql, Object[] values);

	/**
	 * @功能: 分页查询
	 * @作者: yangc
	 * @创建日期: 2013-7-29 下午12:05:33
	 * @param hql
	 * @param values
	 * @return
	 */
	public List find(String hql, Object[] values);

	/**
	 * @功能: 分页查询
	 * @作者: yangc
	 * @创建日期: 2013-7-29 下午12:05:27
	 * @param hql
	 * @param paramMap
	 * @return
	 */
	public List findByMap(String hql, Map<String, Object> paramMap);

	/**
	 * @功能: 全部查询
	 * @作者: yangc
	 * @创建日期: 2013-7-29 下午12:05:20
	 * @param hql
	 * @param values
	 * @return
	 */
	public List findAll(String hql, Object[] values);

	/**
	 * @功能: 全部查询
	 * @作者: yangc
	 * @创建日期: 2013-7-29 下午12:05:27
	 * @param hql
	 * @param paramMap
	 * @return
	 */
	public List findAllByMap(String hql, Map<String, Object> paramMap);

	/**
	 * @功能: 查询记录的数量
	 * @作者: yangc
	 * @创建日期: 2014年1月3日 上午10:07:47
	 * @param hql
	 * @param values
	 * @return
	 */
	public int getCount(String hql, Object[] values);

}
