package com.yangc.shiro.auth;

import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.AuthenticationFilter;

import com.yangc.bean.ResultBean;
import com.yangc.common.StatusCode;
import com.yangc.utils.json.JsonUtils;

public class MyClientFilter extends AuthenticationFilter {

	/**
	 * @功能: 不允许访问时是否自己处理,true表示自己不处理且继续拦截器链执行,false表示自己已经处理了(比如重定向到另一个页面)
	 * @作者: yangc
	 * @创建日期: 2014年10月30日 下午6:45:38
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @see org.apache.shiro.web.filter.AccessControlFilter#onAccessDenied(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletResponse resp = (HttpServletResponse) response;
		resp.setContentType("application/json;charset=UTF-8");
		PrintWriter pw = resp.getWriter();
		pw.write(JsonUtils.toJson(new ResultBean(StatusCode.SESSION_TIMEOUT.value(), false, "session超时")));
		pw.flush();
		pw.close();
		return false;
	}

}
