package com.jpaypp.exception;

public class RateLimitException extends MasJPayException {

	private static final long serialVersionUID = 1L;

	public RateLimitException(String message, Throwable e) {
		super(message, e);
	}

}
