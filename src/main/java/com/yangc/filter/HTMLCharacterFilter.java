package com.yangc.filter;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class HTMLCharacterFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		Map<String, String[]> params = req.getParameterMap();
		if (params != null && !params.isEmpty()) {
			for (Iterator<String> it = params.keySet().iterator(); it.hasNext();) {
				String[] values = params.get(it.next());
				for (int i = 0; i < values.length; i++) {
					values[i] = values[i].replaceAll("<", "&lt;");
					values[i] = values[i].replaceAll(">", "&gt;");
					values[i] = values[i].replaceAll("&", "&amp;");
					values[i] = values[i].replaceAll("\"", "&quot;");
					values[i] = values[i].replaceAll("'", "&apos;");
				}
			}
		}
		chain.doFilter(new ParameterRequestWrapper(req, params), response);
	}

	@Override
	public void destroy() {
	}

}
