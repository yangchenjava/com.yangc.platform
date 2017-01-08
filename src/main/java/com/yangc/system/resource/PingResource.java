package com.yangc.system.resource;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yangc.bean.ResultBean;
import com.yangc.exception.WebApplicationException;
import com.yangc.utils.Constants;
import com.yangc.utils.net.AttachmentUtils;

@Controller
@RequestMapping("/ping")
public class PingResource {

	private static final Logger logger = LogManager.getLogger(PingResource.class);

	/**
	 * @功能: 检查是否通畅
	 * @作者: yangc
	 * @创建日期: 2013年12月23日 下午2:13:04
	 * @return
	 */
	@RequestMapping(value = "system", method = RequestMethod.GET)
	@ResponseBody
	public ResultBean system() {
		logger.info("system");
		try {
			return new ResultBean(true, "success");
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

	@RequestMapping(value = "test", method = RequestMethod.GET)
	@ResponseBody
	public ResultBean test(@DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
		logger.info("test");
		try {
			return new ResultBean(true, DateFormatUtils.format(date, "yyyy-MM-dd"));
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

	@RequestMapping(value = "download", method = RequestMethod.GET)
	public void download(HttpServletRequest request, HttpServletResponse response) {
		logger.info("download");
		String path = new File(request.getSession().getServletContext().getRealPath("/")).getParent() + Constants.PORTRAIT_PATH;
		File file = new File(path, "113_1414850458106_original.png");

		response.reset();
		response.setCharacterEncoding("UTF-8");
		response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		AttachmentUtils.convertAttachmentFileName(request, response, "你好 我是杨晨.png");
		response.setHeader("Content-Length", "" + file.length());
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			out.write(FileUtils.readFileToByteArray(file));
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
