package com.hand.hrms4android.exception;

public class PersistanceException extends Exception {

	/**
     * 
     */
	private static final long serialVersionUID = -4555461302460873443L;

	public PersistanceException() {
	}

	public PersistanceException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public PersistanceException(String detailMessage) {
		super(detailMessage);
	}

	public PersistanceException(Throwable throwable) {
		super(throwable);
	}

}
