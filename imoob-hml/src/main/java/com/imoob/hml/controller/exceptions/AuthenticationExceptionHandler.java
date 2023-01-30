package com.imoob.hml.controller.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.imoob.hml.model.exceptions.StandardError;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class AuthenticationExceptionHandler {
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<StandardError> badCredentials(BadCredentialsException e, HttpServletRequest request){
		String error = "Bad Credentials. Try again.";
		HttpStatus status = HttpStatus.FORBIDDEN;
		
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), e.getCause().getMessage(), request.getRequestURI());
	
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<StandardError> expiredJwt(ExpiredJwtException e, HttpServletRequest request){
		String error = "JWT Expired..";
		HttpStatus status = HttpStatus.FORBIDDEN;
		
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), e.getCause().getMessage(), request.getRequestURI());
	
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(DisabledException.class)
	public ResponseEntity<StandardError> userDisabled(DisabledException e, HttpServletRequest request) {
		String error = "O usuário está desabilitado.";
		HttpStatus status = HttpStatus.FORBIDDEN;
		StandardError err = StandardError.builder()
				.timestamp(Instant.now())
				.status(status.value())
				.message(e.getMessage())
				.error(error)
				.path(request.getRequestURI()).build();
	
		return ResponseEntity.status(status).body(err);
	}
}
