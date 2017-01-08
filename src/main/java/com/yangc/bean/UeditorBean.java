package com.yangc.bean;

public class UeditorBean {

	public static final String SUCCESS = "SUCCESS";
	public static final String FAIL = "FAIL";

	private String state;
	private String title;
	private String original;
	private String type;
	private String size;
	private String url;

	public UeditorBean() {
	}

	public UeditorBean(String state) {
		this.state = state;
	}

	public UeditorBean(String state, String title, String original, String type, String size, String url) {
		this.state = state;
		this.title = title;
		this.original = original;
		this.type = type;
		this.size = size;
		this.url = url;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
