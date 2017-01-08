package com.yangc.bean;

public class ProgressBean {

	/** 当前读取文件的比特数(字节B) */
	private long readBytes;

	/** 文件总大小(字节B) */
	private long contentLength;

	/** 当前正在读取第几个文件 */
	private int readItems;

	/** 当前下载百分比(0-100) */
	private int percent;

	public long getReadBytes() {
		return readBytes;
	}

	public void setReadBytes(long readBytes) {
		this.readBytes = readBytes;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public int getReadItems() {
		return readItems;
	}

	public void setReadItems(int readItems) {
		this.readItems = readItems;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

}
