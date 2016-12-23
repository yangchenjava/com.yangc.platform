package com.yangc.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;

import com.yangc.filter.wrapper.RequestWrapper;
import com.yangc.filter.wrapper.ResponseWrapper;

public class LoggerFilter extends OncePerRequestFilter {

	private static final Logger logger = LogManager.getLogger(LoggerFilter.class);

	private static final String REQUEST_PREFIX = "Request: ";
	private static final String RESPONSE_PREFIX = "Response: ";
	private AtomicLong id = new AtomicLong(1);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
		long requestId = id.getAndIncrement();
		request = new RequestWrapper(requestId, request);
		response = new ResponseWrapper(requestId, response);
		try {
			filterChain.doFilter(request, response);
		} finally {
			if (logger.isInfoEnabled()) {
				logRequest(request);
				logResponse((ResponseWrapper) response);
			}
		}
	}

	private void logRequest(HttpServletRequest request) {
		StringBuilder msg = new StringBuilder(REQUEST_PREFIX);
		if (request instanceof RequestWrapper) {
			msg.append("request id=").append(((RequestWrapper) request).getId()).append("; ");
		}
		HttpSession session = request.getSession(false);
		if (session != null) {
			msg.append("session id=").append(session.getId()).append("; ");
		}
		if (request.getMethod() != null) {
			msg.append("method=").append(request.getMethod()).append("; ");
		}
		if (request.getContentType() != null) {
			msg.append("content type=").append(request.getContentType()).append("; ");
		}
		msg.append("uri=").append(request.getRequestURI());
		if (request.getQueryString() != null) {
			msg.append('?').append(request.getQueryString());
		}

		if (request instanceof RequestWrapper && !isMultipart(request) && !isBinaryContent(request)) {
			RequestWrapper requestWrapper = (RequestWrapper) request;
			try {
				String characterEncoding = StringUtils.isBlank(requestWrapper.getCharacterEncoding()) ? "UTF-8" : requestWrapper.getCharacterEncoding();
				msg.append("; payload=").append(new String(requestWrapper.toByteArray(), characterEncoding));
			} catch (UnsupportedEncodingException e) {
				logger.warn("Failed to parse request payload", e);
			}
		}
		logger.info(msg.toString());
	}

	private boolean isBinaryContent(HttpServletRequest request) {
		if (StringUtils.isBlank(request.getContentType())) {
			return false;
		}
		return request.getContentType().startsWith("image") || request.getContentType().startsWith("video") || request.getContentType().startsWith("audio");
	}

	private boolean isMultipart(HttpServletRequest request) {
		return StringUtils.startsWith(request.getContentType(), "multipart/form-data");
	}

	private void logResponse(ResponseWrapper response) {
		StringBuilder msg = new StringBuilder(RESPONSE_PREFIX);
		msg.append("request id=").append((response.getId()));
		try {
			msg.append("; payload=").append(new String(response.toByteArray(), response.getCharacterEncoding()));
		} catch (UnsupportedEncodingException e) {
			logger.warn("Failed to parse response payload", e);
		}
		logger.info(msg.toString());
	}

}
