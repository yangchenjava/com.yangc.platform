package com.yangc.common;

public enum StatusCode {

	/** 正常 */
	NORMAL(0),

	/** 验证码错误 */
	CAPTCHA_ERROR(100),

	/** session超时 */
	SESSION_TIMEOUT(101);

	private int value;

	StatusCode(int value) {
		this.value = value;
	}

	public static StatusCode valueOf(int i) {
		for (StatusCode value : values()) {
			if (value.equals(i)) {
				return value;
			}
		}
		throw new IllegalArgumentException("枚举不存在");
	}

	public boolean equals(int value) {
		return this.value == value;
	}

	public boolean equals(Integer value) {
		if (value != null) {
			return this.value == value.intValue();
		} else {
			return false;
		}
	}

	public int value() {
		return this.value;
	}

}
