package com.yangc.system.resource;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yangc.bean.ResultBean;
import com.yangc.exception.WebApplicationException;
import com.yangc.shiro.utils.ShiroUtils;
import com.yangc.system.bean.TSysPerson;
import com.yangc.system.service.PersonService;
import com.yangc.system.service.UserService;
import com.yangc.utils.Constants;
import com.yangc.utils.encryption.Md5Utils;

@Controller
@RequestMapping("/interface")
public class InterfaceResource {

	private static final Logger logger = Logger.getLogger(InterfaceResource.class);

	@Autowired
	private UserService userService;
	@Autowired
	private PersonService personService;

	@RequestMapping(value = "login", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean login(String username, String password) {
		logger.info("login - username=" + username + ", password=" + password);
		ResultBean resultBean = new ResultBean();
		try {
			SecurityUtils.getSubject().login(new UsernamePasswordToken(username, Md5Utils.getMD5(password)));
			resultBean.setSuccess(true);
			resultBean.setMessage("" + ShiroUtils.getCurrentUser().getId());
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
	public ResultBean register(String username, String password, String name, Long sex, String phone, String signature, MultipartFile photo, HttpServletRequest request) {
		logger.info("register - username=" + username + ", password=" + password + ", name=" + name + ", sex=" + sex + ", phone=" + phone + ", signature=" + signature);
		ResultBean resultBean = new ResultBean();
		try {
			TSysPerson person = new TSysPerson();
			person.setName(name);
			person.setSex(sex);
			person.setPhone(phone);
			person.setSignature(signature);

			String savePath = new File(request.getSession().getServletContext().getRealPath("/")).getParent() + Constants.PORTRAIT_PATH;
			String urlPath = ".." + Constants.PORTRAIT_PATH;
			this.userService.addOrUpdateUser(null, username, Md5Utils.getMD5(password), person, photo, savePath, urlPath, null);

			SecurityUtils.getSubject().login(new UsernamePasswordToken(username, Md5Utils.getMD5(password)));
			resultBean.setSuccess(true);
			resultBean.setMessage("" + ShiroUtils.getCurrentUser().getId());
			return resultBean;
		} catch (IllegalStateException e) {
			resultBean.setSuccess(false);
			resultBean.setMessage(e.getMessage());
			return resultBean;
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

	@RequestMapping(value = "updatePerson", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean updatePerson(Long userId, String name, Long sex, String phone, String signature, MultipartFile photo, HttpServletRequest request) {
		logger.info("updatePerson - userId=" + userId + ", name=" + name + ", sex=" + sex + ", phone=" + phone + ", signature=" + signature);
		try {
			TSysPerson person = new TSysPerson();
			person.setUserId(userId);
			person.setName(name);
			person.setSex(sex);
			person.setPhone(phone);
			person.setSignature(signature);

			String savePath = new File(request.getSession().getServletContext().getRealPath("/")).getParent() + Constants.PORTRAIT_PATH;
			String urlPath = ".." + Constants.PORTRAIT_PATH;

			this.personService.addOrUpdatePerson(person, photo, savePath, urlPath);
			return new ResultBean(true, "");
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

	@RequestMapping(value = "test", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean test() {
		logger.info("test");
		try {
			return new ResultBean(true, "test - yangchen");
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

}
