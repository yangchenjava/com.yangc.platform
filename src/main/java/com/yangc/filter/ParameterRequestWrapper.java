package com.yangc.filter;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang3.ArrayUtils;

public class ParameterRequestWrapper extends HttpServletRequestWrapper {

	private Map<String, String[]> params;

	public ParameterRequestWrapper(HttpServletRequest request, Map<String, String[]> params) {
		super(request);
		this.params = params;
	}

	@Override
	public String getParameter(String name) {
		String[] param = params.get(name);
		if (ArrayUtils.isNotEmpty(param)) {
			return param[0];
		}
		return null;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return params;
	}

	@Override
	public String[] getParameterValues(String name) {
		return params.get(name);
	}

}
