package com.yangc.filter.wrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.input.TeeInputStream;

public class RequestWrapper extends HttpServletRequestWrapper {

	private long id;
	private final ByteArrayOutputStream bos = new ByteArrayOutputStream();

	public RequestWrapper(long requestId, HttpServletRequest request) {
		super(request);
		this.id = requestId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @功能: 返回http中body内容的拷贝
	 * @作者: yangc
	 * @创建日期: 2016年5月18日 下午3:58:13
	 * @return
	 */
	public byte[] toByteArray() {
		return bos.toByteArray();
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new ServletInputStream() {
			private TeeInputStream tee = new TeeInputStream(RequestWrapper.super.getInputStream(), bos);

			@Override
			public int read() throws IOException {
				return tee.read();
			}
		};
	}

}
