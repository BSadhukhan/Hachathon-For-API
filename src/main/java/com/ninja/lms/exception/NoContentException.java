package com.ninja.lms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class NoContentException extends RuntimeException{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4529421330073865067L;

	public NoContentException(String exception) {
		super(exception);
		// TODO Auto-generated constructor stub
		
	}
	
	

}
