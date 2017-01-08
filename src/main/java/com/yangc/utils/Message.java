package com.yangc.utils;

public class Message {

	/**
	 * 获取属性值
	 * @param key
	 * @return
	 */
	public static String getMessage(String key) {
		return PropertiesFileScanUtils.getMessage(key);
	}

}
