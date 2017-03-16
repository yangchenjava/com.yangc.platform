package com.yangc.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.yangc.common.Pagination;
import com.yangc.common.PaginationThreadUtils;

public class PaginationInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LogManager.getLogger(PaginationInterceptor.class);

	// 前端js对分页请求的名字
	private static final String PAGE_SIZE = "limit";
	private static final String PAGE_NOW = "page";

	@Override
	@SuppressWarnings("unchecked")
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			if (handlerMethod.getMethodAnnotation(com.yangc.annotation.Pagination.class) != null) {
				Pagination pagination = PaginationThreadUtils.get();
				if (pagination == null) {
					pagination = new Pagination();
					PaginationThreadUtils.set(pagination);
				}
				Map<String, String[]> params = request.getParameterMap();
				// 设置要跳转到的页数
				if (params.get(PAGE_NOW) == null) {
					pagination.setPageNow(1);
				} else {
					String pageNow = params.get(PAGE_NOW)[0];
					if (StringUtils.isBlank(pageNow)) {
						pagination.setPageNow(1);
					} else {
						pagination.setPageNow(NumberUtils.toInt(pageNow, 1));
					}
				}
				// 设置每页的行数
				if (params.get(PAGE_SIZE) != null) {
					String pageSize = params.get(PAGE_SIZE)[0];
					if (StringUtils.isNotBlank(pageSize)) {
						pagination.setPageSize(NumberUtils.toInt(pageSize));
					}
				}
				logger.info("PaginationInterceptor - pageNow={}, pageSize={}", pagination.getPageNow(), pagination.getPageSize());
			}
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		PaginationThreadUtils.clear();
	}

}
