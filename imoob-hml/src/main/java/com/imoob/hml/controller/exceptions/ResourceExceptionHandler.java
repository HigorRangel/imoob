package com.imoob.hml.controller.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.imoob.hml.model.exceptions.StandardError;
import com.imoob.hml.service.exceptions.DatabaseException;
import com.imoob.hml.service.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request){
		String error = "Recurso não encontrado.";
		HttpStatus status = HttpStatus.NOT_FOUND;
		
		StandardError err = StandardError.builder()
			.timestamp(Instant.now())
			.status(status.value())
			.error(error)
			.message(e.getMessage())
			.path(request.getRequestURI())
			.build();
		
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandardError> database(DatabaseException e, HttpServletRequest request){
		String error = "Database Error.";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		StandardError err = StandardError.builder()
			.timestamp(Instant.now())
			.status(status.value())
			.error(error)
			.message(e.getMessage())
			.path(request.getRequestURI())
			.build();
		
		return ResponseEntity.status(status).body(err);
	}
}
