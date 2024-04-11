package com.retail.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.retail.model.CustomErrorResponse;

@RestControllerAdvice
public class RetailServiceExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<CustomErrorResponse> handleGlobalException(Exception e) {
		CustomErrorResponse errorResponse = new CustomErrorResponse();
		errorResponse.setErrorMessage(e.getMessage());
		errorResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<CustomErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(value = IllegalArgumentException.class)
	public ResponseEntity<CustomErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
		CustomErrorResponse errorResponse = new CustomErrorResponse();
		errorResponse.setErrorMessage(e.getMessage());
		errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
}
