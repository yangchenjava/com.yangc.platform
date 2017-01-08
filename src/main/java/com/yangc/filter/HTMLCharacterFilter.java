package com.yangc.filter;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.MapUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.yangc.filter.wrapper.ParameterRequestWrapper;

public class HTMLCharacterFilter extends OncePerRequestFilter {

	@Override
	@SuppressWarnings("unchecked")
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		Map<String, String[]> parameterMapCopy = request.getParameterMap();
		if (MapUtils.isNotEmpty(parameterMapCopy)) {
			for (Iterator<String> it = parameterMapCopy.keySet().iterator(); it.hasNext();) {
				String[] values = parameterMapCopy.get(it.next());
				for (int i = 0; i < values.length; i++) {
					values[i] = values[i].replaceAll("&", "&amp;");
					values[i] = values[i].replaceAll("<", "&lt;");
					values[i] = values[i].replaceAll(">", "&gt;");
					values[i] = values[i].replaceAll("\"", "&quot;");
					values[i] = values[i].replaceAll("'", "&apos;");
				}
			}
			filterChain.doFilter(new ParameterRequestWrapper(request, parameterMapCopy), response);
		} else {
			filterChain.doFilter(request, response);
		}
	}

}
