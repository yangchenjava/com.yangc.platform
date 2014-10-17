package com.yangc.system.resource;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yangc.bean.ResultBean;
import com.yangc.exception.WebApplicationException;
import com.yangc.system.bean.TSysPerson;
import com.yangc.system.service.UserService;
import com.yangc.utils.encryption.Md5Utils;

@Controller
@RequestMapping("/interface")
public class InterfaceResource {

	private static final Logger logger = Logger.getLogger(InterfaceResource.class);

	@Autowired
	private UserService userService;

	@RequestMapping(value = "login", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean login(String username, String password) {
		logger.info("login - username=" + username + ", password=" + password);
		ResultBean resultBean = new ResultBean();
		try {
			Subject subject = SecurityUtils.getSubject();
			subject.login(new UsernamePasswordToken(username, Md5Utils.getMD5(password)));
			resultBean.setSuccess(true);
			return resultBean;
		} catch (AuthenticationException e) {
			resultBean.setSuccess(false);
			resultBean.setMessage(e.getMessage());
			return resultBean;
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

	@RequestMapping(value = "register", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean register(String username, String password, String name, Long sex, String phone, String description, MultipartFile photo, HttpServletRequest request) {
		logger.info("register - username=" + username + ", password=" + password + ", name=" + name + ", sex=" + sex + ", phone=" + phone + ", description=" + description);
		TSysPerson person = new TSysPerson();
		person.setName(name);
		person.setSex(sex);
		person.setPhone(phone);
		this.userService.addOrUpdateUser(null, username, Md5Utils.getMD5(password), person, null);
		return null;
	}

}
