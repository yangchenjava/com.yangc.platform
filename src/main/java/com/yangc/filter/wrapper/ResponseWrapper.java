package com.yangc.filter.wrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.io.output.TeeOutputStream;

public class ResponseWrapper extends HttpServletResponseWrapper {

	private long id;
	private final ByteArrayOutputStream bos = new ByteArrayOutputStream();
	private PrintWriter writer = new PrintWriter(bos);

	public ResponseWrapper(long requestId, HttpServletResponse response) {
		super(response);
		this.id = requestId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public byte[] toByteArray() {
		return bos.toByteArray();
	}

	@Override
	public ServletResponse getResponse() {
		return this;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return new ServletOutputStream() {
			private TeeOutputStream tee = new TeeOutputStream(ResponseWrapper.super.getOutputStream(), bos);

			@Override
			public void write(int b) throws IOException {
				tee.write(b);
			}
		};
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return new TeePrintWriter(super.getWriter(), writer);
	}

	private class TeePrintWriter extends PrintWriter {
		private PrintWriter branch;

		public TeePrintWriter(PrintWriter main, PrintWriter branch) {
			super(main, true);
			this.branch = branch;
		}

		@Override
		public void write(char buf[], int off, int len) {
			super.write(buf, off, len);
			super.flush();
			branch.write(buf, off, len);
			branch.flush();
		}

		@Override
		public void write(String s, int off, int len) {
			super.write(s, off, len);
			super.flush();
			branch.write(s, off, len);
			branch.flush();
		}

		@Override
		public void write(int c) {
			super.write(c);
			super.flush();
			branch.write(c);
			branch.flush();
		}

		@Override
		public void flush() {
			super.flush();
			branch.flush();
		}
	}

}
