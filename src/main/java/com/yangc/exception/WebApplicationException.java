package com.yangc.exception;

import com.yangc.bean.ResultBean;

public class WebApplicationException {

	private static final ResultBean resultBean = new ResultBean(false, "服务器异常");

	public static ResultBean build() {
		return resultBean;
	}

}
