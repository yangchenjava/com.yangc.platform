package com.yangc.interceptor;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import com.yangc.bean.BaseBean;
import com.yangc.shiro.utils.ShiroUtils;

public class HibernateInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = 799213033818397971L;

	/**
	 * @功能: 更新时执行
	 * @作者: yangc
	 * @创建日期: 2013-7-24 下午03:56:12
	 */
	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
		if (entity instanceof BaseBean) {
			BaseBean bean = (BaseBean) entity;
			boolean b = false;
			for (int i = 0, length = propertyNames.length; i < length; i++) {
				if (propertyNames[i].equalsIgnoreCase("updateTime")) {
					b = true;
					Date updateTime = new Date();
					currentState[i] = updateTime;
					bean.setUpdateTime(updateTime);
				} else if (propertyNames[i].equalsIgnoreCase("updateUser")) {
					b = true;
					Long userId = ShiroUtils.getCurrentUser().getId();
					currentState[i] = userId;
					bean.setUpdateUser(userId);
				}
			}
			return b;
		}
		return false;
	}

	/**
	 * @功能: 保存时执行
	 * @作者: yangc
	 * @创建日期: 2013-7-24 下午03:56:48
	 */
	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		if (entity instanceof BaseBean) {
			BaseBean bean = (BaseBean) entity;
			boolean b = false;
			for (int i = 0, length = propertyNames.length; i < length; i++) {
				if (propertyNames[i].equalsIgnoreCase("createTime")) {
					b = true;
					Date createTime = new Date();
					state[i] = createTime;
					bean.setCreateTime(createTime);
				} else if (propertyNames[i].equalsIgnoreCase("createUser")) {
					b = true;
					Long userId = ShiroUtils.getCurrentUser().getId();
					state[i] = userId;
					bean.setCreateUser(userId);
				}
			}
			return b;
		}
		return false;
	}

}
