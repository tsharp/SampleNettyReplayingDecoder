package com.orbitalforge.example.net.exceptions;

public class BadMessageException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public BadMessageException() {
		super();
	}
	
	public BadMessageException(String msg) {
		super(msg);
	}
	
	public BadMessageException(Throwable ex) {
		super(ex);
	}
	
	public BadMessageException(String msg, Throwable ex) {
		super(msg, ex);
	}
}
