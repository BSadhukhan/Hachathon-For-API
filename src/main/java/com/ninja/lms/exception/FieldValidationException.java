package com.ninja.lms.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FieldValidationException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1708250715668975634L;

	public FieldValidationException(String exception) {
		super(exception);
		// TODO Auto-generated constructor stub
		
	}
	
	public FieldValidationException(List<String> exceptionList) {
		
	}

}
