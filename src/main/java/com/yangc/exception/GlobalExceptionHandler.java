package com.yangc.exception;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.yangc.bean.ResultBean;
import com.yangc.utils.json.JsonUtils;

public class GlobalExceptionHandler implements HandlerExceptionResolver {

	public static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		logger.error(ex.getMessage(), ex.getCause());
		if (ex instanceof UnauthorizedException) {
			String header = request.getHeader("X-Requested-With");
			// 异步
			if (StringUtils.isNotBlank(header) && (header.equals("X-Requested-With") || header.equals("XMLHttpRequest"))) {
				response.reset();
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json;charset=UTF-8");
				PrintWriter pw = null;
				try {
					pw = response.getWriter();
					pw.write(JsonUtils.toJson(new ResultBean(false, "没有权限")));
					pw.flush();
					pw.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (pw != null) pw.close();
				}
				return null;
			}
		}
		return new ModelAndView("error/exception");
	}

}
