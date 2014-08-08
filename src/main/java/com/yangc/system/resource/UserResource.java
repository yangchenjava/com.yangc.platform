package com.yangc.system.resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yangc.bean.ResultBean;
import com.yangc.exception.WebApplicationException;
import com.yangc.shiro.utils.ShiroUtils;
import com.yangc.system.bean.TSysUser;
import com.yangc.system.service.UserService;
import com.yangc.utils.Constants;
import com.yangc.utils.Message;
import com.yangc.utils.encryption.Md5Utils;
import com.yangc.utils.image.CaptchaUtils;
import com.yangc.utils.image.CaptchaUtils.CAPTCHA_TYPE;

@Controller
@RequestMapping("/user")
public class UserResource {

	private static final Logger logger = Logger.getLogger(UserResource.class);

	@Autowired
	private UserService userService;

	/**
	 * @功能: 校验登录
	 * @作者: yangc
	 * @创建日期: 2012-9-10 上午12:04:21
	 * @return
	 */
	@RequestMapping(value = "login", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean login(String username, String password) {
		logger.info("login - username=" + username + ", password=" + password);
		ResultBean resultBean = new ResultBean();
		Session session = null;
		try {
			Subject subject = SecurityUtils.getSubject();
			session = subject.getSession();
			subject.login(new UsernamePasswordToken(username, Md5Utils.getMD5(password)));
			session.removeAttribute(Constants.ENTER_COUNT);
			session.removeAttribute(Constants.NEED_CAPTCHA);
			resultBean.setSuccess(true);
			resultBean.setMessage(Constants.INDEX_PAGE);
			return resultBean;
		} catch (AuthenticationException e) {
			resultBean.setSuccess(false);
			resultBean.setMessage(e.getMessage());
			if ((Integer) session.getAttribute(Constants.ENTER_COUNT) >= Integer.parseInt(Message.getMessage("shiro.captcha"))) {
				session.setAttribute(Constants.NEED_CAPTCHA, "NEED_CAPTCHA");
				resultBean.setOther("captcha");
			}
			return resultBean;
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

	/**
	 * @功能: 退出系统
	 * @作者: yangc
	 * @创建日期: 2012-9-10 上午12:04:33
	 * @return
	 */
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout() {
		logger.info("logout");
		// session会销毁,在SessionListener监听session销毁,清理权限缓存
		SecurityUtils.getSubject().logout();
		return "redirect:/" + Constants.LOGIN_PAGE;
	}

	/**
	 * @功能: 生成验证码
	 * @作者: yangc
	 * @创建日期: 2012-9-10 上午12:04:33
	 * @return
	 */
	@RequestMapping(value = "captcha", method = RequestMethod.GET)
	public void captcha(HttpServletRequest request, HttpServletResponse response) {
		logger.info("captcha");
		CaptchaUtils.captcha(100, 28, 4, CAPTCHA_TYPE.ALL, request, response);
	}

	/**
	 * @功能: 修改密码
	 * @作者: yangc
	 * @创建日期: 2013年12月21日 下午4:24:12
	 * @return
	 */
	@RequestMapping(value = "changePassword", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean changePassword(String password, String newPassword) {
		ResultBean resultBean = new ResultBean();
		try {
			TSysUser user = ShiroUtils.getCurrentUser();
			password = Md5Utils.getMD5(password);
			newPassword = Md5Utils.getMD5(newPassword);
			logger.info("changePassword - userId=" + user.getId() + ", password=" + password + ", newPassword=" + newPassword);
			if (StringUtils.isBlank(password) || StringUtils.isBlank(newPassword)) {
				resultBean.setSuccess(false);
				resultBean.setMessage("原密码或新密码不能为空");
			} else {
				if (!user.getPassword().equals(password)) {
					resultBean.setSuccess(false);
					resultBean.setMessage("原密码输入错误");
				} else {
					this.userService.updatePassword(user.getId(), newPassword);
					resultBean.setSuccess(true);
					resultBean.setMessage("修改成功");
				}
			}
			return resultBean;
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

	/**
	 * @功能: 校验当前用户密码是否为初始密码
	 * @作者: yangc
	 * @创建日期: 2013年12月21日 下午4:24:12
	 * @return
	 */
	@RequestMapping(value = "checkPassword", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean checkPassword() {
		try {
			TSysUser user = ShiroUtils.getCurrentUser();
			return new ResultBean(user.getPassword().equals(Constants.DEFAULT_PASSWORD), "当前密码为初始密码，建议修改！");
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

}
