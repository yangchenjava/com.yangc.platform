package com.yangc.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.yangc.common.Pagination;
import com.yangc.common.PaginationThreadUtils;

public class PaginationInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = Logger.getLogger(PaginationInterceptor.class);

	// 前端js对分页请求的名字
	private static final String PAGE_SIZE = "limit";
	private static final String PAGE_NOW = "page";

	@Override
	@SuppressWarnings("unchecked")
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String uri = request.getRequestURI();
		if (uri.endsWith("_page")) {
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
					pagination.setPageNow(Integer.parseInt(pageNow));
				}
			}
			// 设置每页的行数
			if (params.get(PAGE_SIZE) != null) {
				String pageSize = params.get(PAGE_SIZE)[0];
				if (StringUtils.isNotBlank(pageSize)) {
					pagination.setPageSize(Integer.parseInt(pageSize));
				}
			}
			logger.info("PaginationInterceptor - pageNow=" + pagination.getPageNow() + ", pageSize=" + pagination.getPageSize());
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		PaginationThreadUtils.clear();
	}

}
