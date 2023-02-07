package com.imoob.hml.controller.exceptions;

import java.time.Instant;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.imoob.hml.model.exceptions.StandardError;
import com.imoob.hml.service.SystemActivityService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
@ControllerAdvice
@RequiredArgsConstructor
public class DatabaseExceptionHandler {
	
	private final SystemActivityService systemService;

	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<StandardError> dataIntegrityViolation(DataIntegrityViolationException e, HttpServletRequest request) throws JsonProcessingException{
		String error = "Erro ao executar a operação no banco de dados.";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		StandardError err = StandardError.builder()
			.timestamp(Instant.now())
			.status(status.value())
			.error(error)
			.message(e.getMessage())
			.path(request.getRequestURI())
			.build();
		
		systemService.insertError(error, status, request, e);

		
		return ResponseEntity.status(status).body(err);
	}
	
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<StandardError> dataIntegrityViolation(UsernameNotFoundException e, HttpServletRequest request) throws JsonProcessingException{
		String error = "Usuário não encontrado. [" + request.getRequestURI() + "]";
		HttpStatus status = HttpStatus.NOT_FOUND;
		
		StandardError err = StandardError.builder()
			.timestamp(Instant.now())
			.status(status.value())
			.error(error)
			.message(e.getMessage())
			.path(request.getRequestURI())
			.build();
		
		systemService.insertError(error, status, request, e);

		
		return ResponseEntity.status(status).body(err);
	}
	
//	
//	@ExceptionHandler(JdbcSQLDataException.class)
//	public ResponseEntity<StandardError> dataIntegrityViolation(DataIntegrityViolationException e, HttpServletRequest request){
//		String error = "Erro ao executar a operação no banco de dados.";
//		HttpStatus status = HttpStatus.BAD_REQUEST;
//		
//		StandardError err = StandardError.builder()
//			.timestamp(Instant.now())
//			.status(status.value())
//			.error(error)
//			.message(e.getMessage())
//			.path(request.getRequestURI())
//			.build();
//		
//		return ResponseEntity.status(status).body(err);
//	}
//	
	
	
}
