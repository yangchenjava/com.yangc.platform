package com.yangc.system.resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yangc.bean.ResultBean;
import com.yangc.exception.WebApplicationException;

@Controller
@RequestMapping("/ping")
public class PingResource {

	private static final Logger logger = Logger.getLogger(PingResource.class);

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
			return new ResultBean(true, "");
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

}
