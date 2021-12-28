package com.ninja.lms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DataNotFoundException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8204877453631747054L;

	public DataNotFoundException(String exception) {
		super(exception);
		// TODO Auto-generated constructor stub
		
	}

}
