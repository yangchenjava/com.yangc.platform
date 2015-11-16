package com.yangc.shiro.auth;

import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.springframework.http.MediaType;

import com.yangc.bean.ResultBean;
import com.yangc.common.StatusCode;
import com.yangc.utils.json.JsonUtils;

public class MyClientFilter extends AuthenticationFilter {

	/**
	 * @功能: 不允许访问时,是否自己处理,true表示自己不处理且继续拦截器链执行,false表示自己已经处理了(比如重定向到另一个页面)
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
		response.reset();
		response.setCharacterEncoding("UTF-8");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		PrintWriter pw = response.getWriter();
		pw.write(JsonUtils.toJson(new ResultBean(StatusCode.SESSION_TIMEOUT, false, "session超时")));
		pw.flush();
		pw.close();
		return false;
	}

}
