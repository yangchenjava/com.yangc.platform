package com.yangc.system.resource;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yangc.bean.UeditorBean;
import com.yangc.system.service.UeditorService;
import com.yangc.utils.Constants;

@Controller
@RequestMapping("/ueditor")
public class UeditorResource {

	private static final Logger logger = Logger.getLogger(UeditorResource.class);

	@Autowired
	private UeditorService ueditorService;

	@RequestMapping(value = "uploadImage", method = RequestMethod.POST)
	@ResponseBody
	public UeditorBean uploadImage(MultipartFile upfile, HttpServletRequest request) {
		logger.info("uploadImage");
		try {
			String savePath = new File(request.getSession().getServletContext().getRealPath("/")).getParent() + Constants.IMAGE_PATH;
			String urlPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + Constants.IMAGE_PATH;
			return this.ueditorService.uploadImage(upfile, savePath, urlPath);
		} catch (Exception e) {
			e.printStackTrace();
			return new UeditorBean(UeditorBean.FAIL);
		}
	}

	@RequestMapping(value = "uploadBase64", method = RequestMethod.POST)
	@ResponseBody
	public UeditorBean uploadBase64(String upfile, HttpServletRequest request) {
		logger.info("uploadBase64");
		try {
			String savePath = new File(request.getSession().getServletContext().getRealPath("/")).getParent() + Constants.IMAGE_PATH;
			String urlPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + Constants.IMAGE_PATH;
			return this.ueditorService.uploadBase64(upfile, savePath, urlPath);
		} catch (Exception e) {
			e.printStackTrace();
			return new UeditorBean(UeditorBean.FAIL);
		}
	}

	@RequestMapping(value = "uploadFile", method = RequestMethod.POST)
	@ResponseBody
	public UeditorBean uploadFile(MultipartFile upfile, HttpServletRequest request) {
		logger.info("uploadFile");
		try {
			String savePath = new File(request.getSession().getServletContext().getRealPath("/")).getParent() + Constants.FILE_PATH;
			String urlPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + Constants.FILE_PATH;
			return this.ueditorService.uploadFile(upfile, savePath, urlPath);
		} catch (Exception e) {
			e.printStackTrace();
			return new UeditorBean(UeditorBean.FAIL);
		}
	}

}
