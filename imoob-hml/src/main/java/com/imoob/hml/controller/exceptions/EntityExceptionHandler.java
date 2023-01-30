package com.imoob.hml.controller.exceptions;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.imoob.hml.model.exceptions.StandardError;
import com.imoob.hml.model.exceptions.ValidationException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class EntityExceptionHandler {

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<StandardError> messageNotReadable(HttpMessageNotReadableException e, HttpServletRequest request){
		String error = "Há campo(s) não preenchido(s) corretamente.";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), e.getCause().getMessage(), request.getRequestURI());
	
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<StandardError> usernameNotFound(UsernameNotFoundException e, HttpServletRequest request){
		String error = "Usuário não encontrado.";
		HttpStatus status = HttpStatus.NOT_FOUND;
		
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), e.getCause().getMessage(), request.getRequestURI());
	
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ValidationException> hibernateValidation(ConstraintViolationException e, HttpServletRequest request){
//		String error = "Campos com os dados incorretos. [" + e.getConstraintViolations();
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		List<Map<String, String>> listErrors = e.getConstraintViolations().stream().map(x -> new HashMap<String, String>(){
			private static final long serialVersionUID = 1L;
		{
			put("attributeName", x.getPropertyPath().toString().toUpperCase());
			put("error", x.getMessage());
			}}).collect(Collectors.toList());
		
		ValidationException err = new ValidationException(Instant.now(), status.value(), request.getRequestURI(), listErrors);
	
		return ResponseEntity.status(status).body(err);
	}
	
	
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<StandardError> nullPointer(NullPointerException e, HttpServletRequest request){
		String error = "Valor nulo.";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), e.getCause().getMessage(), request.getRequestURI());
	
		return ResponseEntity.status(status).body(err);
	}
}
