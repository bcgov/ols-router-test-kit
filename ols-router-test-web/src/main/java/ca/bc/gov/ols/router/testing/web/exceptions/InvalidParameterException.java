/**
 * Copyright 2008-2019, Province of British Columbia
 *  All rights reserved.
 */
package ca.bc.gov.ols.router.testing.web.exceptions;

public class InvalidParameterException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private ErrorMessage message;
	
	public InvalidParameterException(String msg) {
		message = new ErrorMessage(msg);
	}
	
	public InvalidParameterException(ErrorMessage message) {
		this.message = message;
	}
	
	public ErrorMessage getErrorMessage() {
		return message;
	}
}
