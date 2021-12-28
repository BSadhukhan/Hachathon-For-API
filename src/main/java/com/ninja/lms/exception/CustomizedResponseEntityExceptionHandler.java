package com.ninja.lms.exception;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.exception.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ninja.lms.dto.ExceptionResponse;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler{
	
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<ExceptionResponse> handleMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {

	    String provided = ex.getContentType().toString();
	    List<String> supported = ex.getSupportedMediaTypes()
	    		.stream()
	            .map(MimeType::toString)
	            .collect(Collectors.toList());

	    String error = provided + " is not one of the supported media types (" +
	            String.join(", ", supported) + ")";

	    ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), error, ex.getLocalizedMessage());
	    
	    return new ResponseEntity<>(exceptionResponse, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ExceptionResponse> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, WebRequest request) {
	    
		String provided = ex.getMethod();
	    List<String> supported = Arrays.asList(ex.getSupportedMethods());
	    
	    String error = provided + " is not one of the supported Http Methods (" + String.join(", ", supported) + ")";
	    
	    ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), error, ex.getLocalizedMessage());

	    return new ResponseEntity<>(exceptionResponse, HttpStatus.METHOD_NOT_ALLOWED);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ExceptionResponse> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
	    
		String provided = ex.getConstraintName();
		
		System.out.println(ex.getSQL());
	    
	    String error = provided + " is already mapped !!";
	    
	    ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), error, ex.getLocalizedMessage());

	    return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
		
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), errors.toString(), request.getDescription(false));
		
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {

		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getLocalizedMessage(), request.getDescription(false));

		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(FieldValidationException.class)
    public final ResponseEntity<ExceptionResponse> handlerValidationException(FieldValidationException ex, WebRequest request){
 
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
        
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(DataNotFoundException.class)
	public final ResponseEntity<ExceptionResponse> handleDataNotFoundException(DataNotFoundException ex, WebRequest request) {
	    
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
	   
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(NoContentException.class)
	public final ResponseEntity<ExceptionResponse> handleNoContentException(NoContentException ex, WebRequest request){
		System.out.println(ex.getMessage());
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		   
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NO_CONTENT);
	}

}
