package com.yangc.utils;

import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;

import com.yangc.bean.BaseBean;

public class BeanUtils<E> {

	public static <E> List<E> fillingTime(List<E> list) {
		if (list != null) {
			for (E e : list) {
				if (e instanceof BaseBean) {
					BaseBean baseBean = (BaseBean) e;
					if (baseBean.getCreateTime() != null) {
						baseBean.setCreateTimeStr(DateFormatUtils.format(baseBean.getCreateTime(), "yyyy-MM-dd HH:mm"));
					}
					if (baseBean.getUpdateTime() != null) {
						baseBean.setUpdateTimeStr(DateFormatUtils.format(baseBean.getUpdateTime(), "yyyy-MM-dd HH:mm"));
					}
				}
			}
		}
		return list;
	}

}
