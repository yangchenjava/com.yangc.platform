package com.yangc.system.resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yangc.bean.ResultBean;
import com.yangc.exception.WebApplicationException;
import com.yangc.system.service.FileService;

@Controller
@RequestMapping("/file")
public class FileResource {

	private static final Logger logger = Logger.getLogger(FileResource.class);

	@Autowired
	private FileService fileService;

	@RequestMapping(value = "upload", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean upload(@RequestParam("file") MultipartFile file, String test) {
		logger.info("upload");
		try {
			this.fileService.upload(file.getInputStream(), file.getOriginalFilename(), test);
			return new ResultBean(true, "上传成功");
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

}
