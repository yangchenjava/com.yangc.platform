package com.yangc.shiro.auth;

import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.springframework.http.MediaType;

import com.yangc.bean.ResultBean;
import com.yangc.common.StatusCode;
import com.yangc.utils.json.JsonUtils;

public class MyAuthenticationFilter extends AuthenticationFilter {

	/**
	 * @功能: 是否允许访问
	 * @作者: yangc
	 * @创建日期: 2014年10月30日 下午6:44:44
	 * @param request
	 * @param response
	 * @param mappedValue
	 * @return
	 * @see org.apache.shiro.web.filter.authc.AuthenticationFilter#isAccessAllowed(javax.servlet.ServletRequest, javax.servlet.ServletResponse, java.lang.Object)
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		// 是否已经登录成功
		boolean isAuthenticated = this.getSubject(request, response).isAuthenticated();
		// 是否访问的是登录页面(保证了如果已经登录成功了, 再访问登录页面, 后台直接跳转到登录成功后的页面)
		boolean isLoginRequest = this.isLoginRequest(request, response) || this.pathsMatch("/", request);
		if (!isAuthenticated && isLoginRequest) {
			return true;
		} else if (isAuthenticated && !isLoginRequest) {
			return true;
		}
		return false;
	}

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
		boolean isAuthenticated = this.getSubject(request, response).isAuthenticated();
		if (isAuthenticated) {
			this.issueSuccessRedirect(request, response);
		} else {
			HttpServletRequest req = (HttpServletRequest) request;
			String header = req.getHeader("X-Requested-With");
			// 异步
			if (StringUtils.equals(header, "X-Requested-With") || StringUtils.equals(header, "XMLHttpRequest")) {
				response.reset();
				response.setCharacterEncoding("UTF-8");
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				PrintWriter pw = response.getWriter();
				pw.write(JsonUtils.toJson(new ResultBean(StatusCode.SESSION_TIMEOUT, false, "页面超时，请重新登录!")));
				pw.flush();
				pw.close();
			}
			// 同步
			else {
				this.redirectToLogin(request, response);
			}
		}
		return false;
	}

}
