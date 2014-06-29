package com.yangc.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.yangc.bean.BaseBean;
import com.yangc.common.Pagination;
import com.yangc.common.PaginationThreadUtils;
import com.yangc.dao.BaseDao;

public class BaseDaoImpl extends HibernateDaoSupport implements BaseDao {

	@Override
	public Serializable save(BaseBean bean) {
		return this.getHibernateTemplate().save(bean);
	}

	@Override
	public void saveOrUpdate(BaseBean bean) {
		this.getHibernateTemplate().saveOrUpdate(bean);
		this.getHibernateTemplate().flush();
	}

	@Override
	public void saveOrUpdate(List<BaseBean> list) {
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
	public BaseBean get(Class<? extends BaseBean> clazz, Serializable id) {
		if (id != null) {
			return (BaseBean) this.getHibernateTemplate().get(clazz, id);
		}
		return null;
	}

	@Override
	public BaseBean get(String className, Serializable id) {
		if (id != null) {
			return (BaseBean) this.getHibernateTemplate().get(className, id);
		}
		return null;
	}

	@Override
	public BaseBean get(String hql, Object[] values) {
		List<?> list = this.getHibernateTemplate().find(hql, values);
		if (list != null && !list.isEmpty()) {
			return (BaseBean) list.get(0);
		}
		return null;
	}

	@Override
	public List<?> find(final String hql, final Object[] values) {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			@Override
			public List<?> doInHibernate(Session session) throws HibernateException, SQLException {
				/* 获取分页情况 */
				Pagination pagination = PaginationThreadUtils.get();
				if (pagination == null) {
					pagination = new Pagination();
					PaginationThreadUtils.set(pagination);
					pagination.setPageNow(1);
				}
				if (pagination.getTotalCount() == 0) {
					String tempHql = hql.toLowerCase().trim();
					int index = (" " + tempHql).indexOf(" from ");
					int orderIndex = tempHql.indexOf(" order by ");
					String countHql = null;
					if (orderIndex == -1) {
						countHql = "select count(*) " + hql.trim().substring(index);
					} else {
						countHql = "select count(*) " + hql.trim().substring(index, orderIndex);
					}
					Query countQuery = session.createQuery(countHql);
					if (null != values) {
						for (int i = 0, length = values.length; i < length; i++) {
							countQuery.setParameter(i, values[i]);
						}
					}
					Number count = (Number) countQuery.uniqueResult();
					pagination.setTotalCount(count.intValue());
				}
				Query query = session.createQuery(hql);
				/* 设置参数 */
				if (null != values) {
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
	public List<?> findByMap(final String hql, final Map<String, Object> paramMap) {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			@Override
			public List<?> doInHibernate(Session session) throws HibernateException, SQLException {
				/* 获取分页情况 */
				Pagination pagination = PaginationThreadUtils.get();
				if (pagination == null) {
					pagination = new Pagination();
					PaginationThreadUtils.set(pagination);
					pagination.setPageNow(1);
				}
				if (pagination.getTotalCount() == 0) {
					String tempHql = hql.toLowerCase().trim();
					int index = (" " + tempHql).indexOf(" from ");
					int orderIndex = tempHql.indexOf(" order by ");
					String countHql = null;
					if (orderIndex == -1) {
						countHql = "select count(*) " + hql.trim().substring(index);
					} else {
						countHql = "select count(*) " + hql.trim().substring(index, orderIndex);
					}
					Query countQuery = session.createQuery(countHql);
					countQuery.setProperties(paramMap);
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

	@Override
	public List<?> findAll(String hql, Object[] values) {
		return this.getHibernateTemplate().find(hql, values);
	}

	@Override
	public List<?> findAllByMap(final String hql, final Map<String, Object> paramMap) {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<?>>() {
			@Override
			public List<?> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				query.setProperties(paramMap);
				return query.list();
			}
		});
	}

	@Override
	public int getCount(String hql, Object[] values) {
		return ((Number) this.getHibernateTemplate().iterate(hql, values).next()).intValue();
	}

}
