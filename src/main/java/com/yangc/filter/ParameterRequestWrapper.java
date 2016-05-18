package com.yangc.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

public class ParameterRequestWrapper extends HttpServletRequestWrapper {

	private final Map<String, String[]> parameterMapCopy;
	private final byte[] bodyCopy;

	public ParameterRequestWrapper(HttpServletRequest request, Map<String, String[]> parameterMapCopy) {
		super(request);
		this.parameterMapCopy = parameterMapCopy;

		int contentLength = this.getContentLength();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(contentLength >= 0 ? contentLength : 1024);
		try {
			IOUtils.copy(super.getInputStream(), byteArrayOutputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.bodyCopy = byteArrayOutputStream.toByteArray();
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new DefaultServletInputStream(new ByteArrayInputStream(this.bodyCopy));
	}

	@Override
	public String getCharacterEncoding() {
		String enc = super.getCharacterEncoding();
		return enc != null ? enc : "UTF-8";
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(this.getInputStream(), this.getCharacterEncoding()));
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

	/**
	 * @功能: 返回http中body内容的拷贝
	 * @作者: yangc
	 * @创建日期: 2016年5月18日 下午3:58:13
	 * @return
	 */
	public byte[] getBodyCopy() {
		return this.bodyCopy;
	}

	private class DefaultServletInputStream extends ServletInputStream {
		private final ByteArrayInputStream is;

		public DefaultServletInputStream(ByteArrayInputStream is) {
			this.is = is;
		}

		@Override
		public int read() throws IOException {
			return this.is.read();
		}
	}

}
