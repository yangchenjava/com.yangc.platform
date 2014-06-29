package com.yangc.shiro.auth;

import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;

import com.yangc.bean.ResultBean;
import com.yangc.utils.Constants;
import com.yangc.utils.Message;
import com.yangc.utils.image.CaptchaUtils;
import com.yangc.utils.json.JsonUtils;

public class MyCaptchaFilter extends AuthenticationFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		Session session = this.getSubject(request, response).getSession();

		Integer enterCount = (Integer) session.getAttribute(Constants.ENTER_COUNT);
		if (enterCount == null) {
			enterCount = 0;
		}
		if (enterCount < Integer.parseInt(Message.getMessage("shiro.captcha"))) {
			session.setAttribute(Constants.ENTER_COUNT, enterCount + 1);
			return true;
		}

		String code = (String) session.getAttribute(CaptchaUtils.CAPTCHA);
		try {
			session.removeAttribute(CaptchaUtils.CAPTCHA);
			if (StringUtils.equalsIgnoreCase(code, URLDecoder.decode(request.getParameter("captcha"), "UTF-8"))) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter pw = response.getWriter();
		pw.write(JsonUtils.toJson(new ResultBean(false, "验证码错误", "captcha")));
		pw.flush();
		pw.close();
		return false;
	}

}
