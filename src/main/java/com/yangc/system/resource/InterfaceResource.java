package com.yangc.system.resource;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.yangc.system.service.FriendService;
import com.yangc.system.service.PersonService;
import com.yangc.system.service.UserService;
import com.yangc.utils.Constants;
import com.yangc.utils.encryption.Md5Utils;
import com.yangc.utils.json.JsonUtils;

@Controller
@RequestMapping("/interface")
public class InterfaceResource {

	private static final Logger logger = LogManager.getLogger(InterfaceResource.class);

	@Autowired
	private UserService userService;
	@Autowired
	private PersonService personService;
	@Autowired
	private FriendService friendService;

	@RequestMapping(value = "login", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean login(String username, String password) {
		logger.info("login - username={}, password={}", username, password);
		ResultBean resultBean = new ResultBean();
		try {
			SecurityUtils.getSubject().login(new UsernamePasswordToken(username, Md5Utils.getMD5(password)));
			resultBean.setSuccess(true);
			resultBean.setMessage(JsonUtils.toJson(this.personService.getPersonByUserId(ShiroUtils.getCurrentUser().getId())));
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
	public ResultBean register(String username, String password, String nickname, Long sex, String phone, String signature, MultipartFile photo, HttpServletRequest request) {
		logger.info("register - username={}, password={}, nickname={}, sex={}, phone={}, signature={}", username, password, nickname, sex, phone, signature);
		ResultBean resultBean = new ResultBean();
		try {
			TSysPerson person = new TSysPerson();
			person.setNickname(nickname);
			person.setSex(sex);
			person.setPhone(phone);
			person.setSignature(signature);

			String savePath = new File(request.getSession().getServletContext().getRealPath("/")).getParent() + Constants.PORTRAIT_PATH;
			String urlPath = ".." + Constants.PORTRAIT_PATH;
			this.userService.addOrUpdateUser(null, username, Md5Utils.getMD5(password), person, photo, savePath, urlPath, null);

			SecurityUtils.getSubject().login(new UsernamePasswordToken(username, Md5Utils.getMD5(password)));
			resultBean.setSuccess(true);
			resultBean.setMessage(JsonUtils.toJson(person));
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
	public ResultBean updatePerson(Long id, String nickname, Long sex, String phone, String signature, HttpServletRequest request) {
		logger.info("updatePerson - id={}, nickname={}, sex={}, phone={}, signature={}", id, nickname, sex, phone, signature);
		try {
			TSysPerson person = this.personService.getPersonById(id);
			person.setNickname(nickname);
			person.setSex(sex);
			person.setPhone(phone);
			person.setSignature(signature);

			this.personService.addOrUpdatePerson(person, null, null, null);
			return new ResultBean(true, JsonUtils.toJson(person));
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

	@RequestMapping(value = "updatePersonPhoto", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean updatePersonPhoto(Long id, MultipartFile photo, HttpServletRequest request) {
		logger.info("updatePersonPhoto - id={}", id);
		try {
			TSysPerson person = this.personService.getPersonById(id);

			String savePath = new File(request.getSession().getServletContext().getRealPath("/")).getParent() + Constants.PORTRAIT_PATH;
			String urlPath = ".." + Constants.PORTRAIT_PATH;

			this.personService.addOrUpdatePerson(person, photo, savePath, urlPath);
			return new ResultBean(true, JsonUtils.toJson(person));
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

	@RequestMapping(value = "userInfo", method = RequestMethod.POST)
	@ResponseBody
	public TSysPerson userInfo(Long userId) {
		logger.info("userInfo - userId={}", userId);
		return this.personService.getPersonByUserId(userId);
	}

	@RequestMapping(value = "friends", method = RequestMethod.POST)
	@ResponseBody
	public List<TSysPerson> friends(Long userId, String friendIds) {
		logger.info("friends - userId={}, friendIds={}", userId, friendIds);
		if (StringUtils.isNotBlank(friendIds)) {
			this.friendService.delFriend(userId, friendIds);
		}
		return this.friendService.getFriendListByUserId(userId);
	}

	@RequestMapping(value = "deleteFriend", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean deleteFriend(Long userId, String friendId) {
		logger.info("deleteFriend - userId={}, friendId={}", userId, friendId);
		try {
			this.friendService.delFriend(userId, friendId);
			return new ResultBean(true, friendId);
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

}
