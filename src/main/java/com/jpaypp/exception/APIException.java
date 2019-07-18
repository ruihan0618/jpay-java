package com.jpaypp.exception;

public class APIException extends MasJPayException {

	private static final long serialVersionUID = 1L;

	public APIException(String message, Throwable e) {
		super(message, e);
	}

}
