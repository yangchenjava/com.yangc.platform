package com.yangc.dao;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface JdbcDao {

	/** 缓存SQL语句 */
	public static final Map<String, String> SQL_MAPPING = new HashMap<String, String>();

	/**
	 * @功能: 保存或更新
	 * @作者: yangc
	 * @创建日期: 2013-7-29 上午11:54:04
	 * @param sql
	 * @param paramMap
	 * @return
	 */
	public int saveOrUpdate(String sql, Map<String, Object> paramMap);

	/**
	 * @功能: 删除
	 * @作者: yangc
	 * @创建日期: 2013-7-29 上午11:54:38
	 * @param sql
	 * @param paramMap
	 * @return
	 */
	public int delete(String sql, Map<String, Object> paramMap);

	/**
	 * @功能: 批量执行增删改
	 * @作者: yangc
	 * @创建日期: 2013-7-29 上午11:54:48
	 * @param sql
	 * @param paramList
	 * @return
	 */
	public int[] batchExecute(String sql, List<Object[]> paramList);

	/**
	 * @功能: 分页查询
	 * @作者: yangc
	 * @创建日期: 2013-7-29 上午11:55:10
	 * @param sql
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> find(String sql, Map<String, Object> paramMap);

	/**
	 * @功能: 全部查询
	 * @作者: yangc
	 * @创建日期: 2013-7-29 上午11:55:06
	 * @param sql
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> findAll(String sql, Map<String, Object> paramMap);

	/**
	 * @功能: 查询记录的数量
	 * @作者: yangc
	 * @创建日期: 2013-7-29 上午11:54:59
	 * @param sql
	 * @param paramMap
	 * @return
	 */
	public int getCount(String sql, Map<String, Object> paramMap);

	/**
	 * @功能: 获取链接
	 * @作者: yangc
	 * @创建日期: 2013-7-29 上午11:54:54
	 * @return
	 */
	public Connection getConn();

}
