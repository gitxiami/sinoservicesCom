package com.sinoservices.common.exception;
/**XXException自定义异常类**/
public class XXException extends Exception {
	private static final long serialVersionUID = 1L;

	public XXException(String message) {
		super(message);
	}

	public XXException(String message, Throwable cause) {
		super(message, cause);
	}
}
