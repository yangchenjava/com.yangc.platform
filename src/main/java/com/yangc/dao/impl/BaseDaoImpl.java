package com.yangc.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.hql.FilterTranslator;
import org.hibernate.hql.QueryTranslatorFactory;
import org.hibernate.impl.SessionFactoryImpl;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.yangc.bean.BaseBean;
import com.yangc.common.Pagination;
import com.yangc.common.PaginationThreadUtils;
import com.yangc.dao.BaseDao;

@SuppressWarnings("unchecked")
public class BaseDaoImpl extends HibernateDaoSupport implements BaseDao {

	@Override
	public Serializable save(BaseBean bean) {
		Serializable id = this.getHibernateTemplate().save(bean);
		this.getHibernateTemplate().flush();
		return id;
	}

	@Override
	public void saveOrUpdate(BaseBean bean) {
		this.getHibernateTemplate().saveOrUpdate(bean);
		this.getHibernateTemplate().flush();
	}

	@Override
	public void saveOrUpdate(List<? extends BaseBean> list) {
		this.getHibernateTemplate().saveOrUpdateAll(list);
		this.getHibernateTemplate().flush();
	}

	@Override
	public void delete(Class<? extends BaseBean> clazz, Serializable id) {
		Object entity = this.getHibernateTemplate().get(clazz, id);
		this.getHibernateTemplate().delete(entity);
		this.getHibernateTemplate().flush();
	}

	@Override
	public void update(BaseBean bean) {
		this.getHibernateTemplate().update(bean);
		this.getHibernateTemplate().flush();
	}

	@Override
	public void updateOrDelete(String hql, Object[] values) {
		this.getHibernateTemplate().bulkUpdate(hql, values);
		this.getHibernateTemplate().flush();
	}

	@Override
	public void merge(BaseBean bean) {
		this.getHibernateTemplate().merge(bean);
		this.getHibernateTemplate().flush();
	}

	@Override
	public <T> T get(Class<? extends BaseBean> clazz, Serializable id) {
		if (id != null) {
			return (T) this.getHibernateTemplate().get(clazz, id);
		}
		return null;
	}

	@Override
	public <T> T get(String className, Serializable id) {
		if (id != null) {
			return (T) this.getHibernateTemplate().get(className, id);
		}
		return null;
	}

	@Override
	public <T> T get(String hql, Object[] values) {
		List<T> list = this.findAll(hql, values);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public <T> T getByMap(String hql, Map<String, Object> paramMap) {
		List<T> list = this.findAllByMap(hql, paramMap);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public <T> List<T> find(final String hql, final Object[] values) {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<T>>() {
			@Override
			public List<T> doInHibernate(Session session) throws HibernateException, SQLException {
				/* 获取分页情况 */
				Pagination pagination = PaginationThreadUtils.get();
				if (pagination == null) {
					pagination = new Pagination();
					PaginationThreadUtils.set(pagination);
					pagination.setPageNow(1);
				}
				if (pagination.getTotalCount() == 0) {
					String tempHql = hql.toLowerCase().trim();
					Query countQuery = null;
					if (tempHql.indexOf(" group by ") == -1) {
						int fromIndex = (" " + tempHql).indexOf(" from ");
						int orderIndex = tempHql.indexOf(" order by ");
						String countHql = null;
						if (orderIndex == -1) {
							countHql = "select count(*) " + hql.trim().substring(fromIndex);
						} else {
							countHql = "select count(*) " + hql.trim().substring(fromIndex, orderIndex);
						}
						countQuery = session.createQuery(countHql);
					} else {
						countQuery = session.createSQLQuery("SELECT COUNT(1) FROM (" + BaseDaoImpl.this.hql2Sql(hql) + ") TEMP_TABLE_");
					}

					if (values != null) {
						for (int i = 0, length = values.length; i < length; i++) {
							countQuery.setParameter(i, values[i]);
						}
					}
					Number count = (Number) countQuery.uniqueResult();
					pagination.setTotalCount(count.intValue());
				}
				Query query = session.createQuery(hql);
				/* 设置参数 */
				if (values != null) {
					for (int i = 0, length = values.length; i < length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				int firstResult = (pagination.getPageNow() - 1) * pagination.getPageSize();
				int maxResults = pagination.getPageSize();
				/* 校验分页情况 */
				if (firstResult >= pagination.getTotalCount() || firstResult < 0) {
					firstResult = 0;
					pagination.setPageNow(1);
				}
				if (firstResult >= 0) {
					query.setFirstResult(firstResult);
				}
				if (maxResults > 0) {
					query.setMaxResults(maxResults);
				}
				return query.list();
			}
		});
	}

	@Override
	public <T> List<T> findByMap(final String hql, final Map<String, Object> paramMap) {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<T>>() {
			@Override
			public List<T> doInHibernate(Session session) throws HibernateException, SQLException {
				/* 获取分页情况 */
				Pagination pagination = PaginationThreadUtils.get();
				if (pagination == null) {
					pagination = new Pagination();
					PaginationThreadUtils.set(pagination);
					pagination.setPageNow(1);
				}
				if (pagination.getTotalCount() == 0) {
					String tempHql = hql.toLowerCase().trim();
					Query countQuery = null;
					if (tempHql.indexOf(" group by ") == -1) {
						int fromIndex = (" " + tempHql).indexOf(" from ");
						int orderIndex = tempHql.indexOf(" order by ");
						String countHql = null;
						if (orderIndex == -1) {
							countHql = "select count(*) " + hql.trim().substring(fromIndex);
						} else {
							countHql = "select count(*) " + hql.trim().substring(fromIndex, orderIndex);
						}
						countQuery = session.createQuery(countHql);
						countQuery.setProperties(paramMap);
					} else {
						countQuery = session.createSQLQuery("SELECT COUNT(1) FROM (" + BaseDaoImpl.this.hql2Sql(hql) + ") TEMP_TABLE_");
						Object[] values = BaseDaoImpl.this.paramMap2Array(hql, paramMap);
						for (int i = 0, length = values.length; i < length; i++) {
							countQuery.setParameter(i, values[i]);
						}
					}

					Number count = (Number) countQuery.uniqueResult();
					pagination.setTotalCount(count.intValue());
				}
				Query query = session.createQuery(hql);
				/* 设置参数 */
				query.setProperties(paramMap);
				int firstResult = (pagination.getPageNow() - 1) * pagination.getPageSize();
				int maxResults = pagination.getPageSize();
				/* 校验分页情况 */
				if (firstResult >= pagination.getTotalCount() || firstResult < 0) {
					firstResult = 0;
					pagination.setPageNow(1);
				}
				if (firstResult >= 0) {
					query.setFirstResult(firstResult);
				}
				if (maxResults > 0) {
					query.setMaxResults(maxResults);
				}
				return query.list();
			}
		});
	}

	/**
	 * @功能: map参数转为数组参数
	 * @作者: yangc
	 * @创建日期: 2016年6月9日 上午1:03:05
	 * @param hql
	 * @param map
	 * @return
	 */
	private Object[] paramMap2Array(String hql, Map<String, Object> map) {
		List<Object> list = new ArrayList<Object>();
		if (map != null && map.size() != 0) {
			for (int i = 0; i < hql.length(); i++) {
				if (hql.charAt(i) == ':') {
					int blankIndex = hql.indexOf(" ", i);
					list.add(map.get(hql.substring(i + 1, blankIndex)));
					i = blankIndex;
				}
			}
		}
		return list.toArray();
	}

	/**
	 * @功能: hql转为sql
	 * @作者: yangc
	 * @创建日期: 2016年6月9日 上午1:03:57
	 * @param hql
	 * @return
	 */
	private String hql2Sql(String hql) {
		SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) this.getSessionFactory();
		QueryTranslatorFactory queryTranslatorFactory = sessionFactoryImpl.getSettings().getQueryTranslatorFactory();
		FilterTranslator filterTranslator = queryTranslatorFactory.createFilterTranslator(hql, hql, Collections.EMPTY_MAP, sessionFactoryImpl);
		filterTranslator.compile(null, false);
		return filterTranslator.getSQLString();
	}

	@Override
	public <T> List<T> findAll(String hql, Object[] values) {
		return (List<T>) this.getHibernateTemplate().find(hql, values);
	}

	@Override
	public <T> List<T> findAllByMap(final String hql, final Map<String, Object> paramMap) {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<T>>() {
			@Override
			public List<T> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				query.setProperties(paramMap);
				return query.list();
			}
		});
	}

	@Override
	public int getCount(String hql, Map<String, Object> paramMap) {
		Number count = (Number) this.findAllByMap(hql, paramMap).get(0);
		if (count != null) {
			return count.intValue();
		}
		return 0;
	}

}
