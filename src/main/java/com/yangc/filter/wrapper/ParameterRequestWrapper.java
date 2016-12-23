package com.yangc.filter.wrapper;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class ParameterRequestWrapper extends RequestWrapper {

	private final Map<String, String[]> parameterMapCopy;

	public ParameterRequestWrapper(HttpServletRequest request, Map<String, String[]> parameterMapCopy) {
		super(0, request);
		this.parameterMapCopy = parameterMapCopy;
	}

	@Override
	public String getParameter(String name) {
		String[] values = this.parameterMapCopy.get(name);
		if (values != null && values.length != 0) {
			return values[0];
		}
		return null;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return this.parameterMapCopy;
	}

	@Override
	public String[] getParameterValues(String name) {
		return this.parameterMapCopy.get(name);
	}

}
