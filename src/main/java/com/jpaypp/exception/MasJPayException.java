package com.jpaypp.exception;

public abstract class MasJPayException extends Exception {

	public MasJPayException(String message) {
		super(message, null);
	}

	public MasJPayException(String message, Throwable e) {
		super(message, e);
	}

	private static final long serialVersionUID = 1L;

}
