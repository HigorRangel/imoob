package com.imoob.hml.controller.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.imoob.hml.model.exceptions.StandardError;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class EntityExceptionHandler {

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<StandardError> messageNotReadable(HttpMessageNotReadableException e, HttpServletRequest request){
		String error = "Há campo(s) não preenchido(s) corretamente.";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
	
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<StandardError> usernameNotFound(UsernameNotFoundException e, HttpServletRequest request){
		String error = "Usuário não encontrado.";
		HttpStatus status = HttpStatus.NOT_FOUND;
		
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
	
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<StandardError> hibernateValidation(ConstraintViolationException e, HttpServletRequest request){
		String error = "Campos com os dados incorretos. [" + e.getConstraintViolations();
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
	
		return ResponseEntity.status(status).body(err);
	}
}
