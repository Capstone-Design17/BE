package com.tomato.market.handler.exception;

public class UserException extends RuntimeException {
	UserException() {

	}

	public UserException(String message) {
		super(message);
	}
}
