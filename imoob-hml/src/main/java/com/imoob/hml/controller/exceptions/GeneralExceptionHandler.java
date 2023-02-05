package com.imoob.hml.controller.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.imoob.hml.model.RealEstate;
import com.imoob.hml.model.SystemActivity;
import com.imoob.hml.model.enums.SystemOperation;
import com.imoob.hml.model.exceptions.StandardError;
import com.imoob.hml.service.SystemActivityService;
import com.imoob.hml.service.exceptions.GeneralException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class GeneralExceptionHandler {
	private final SystemActivityService systemService;

	@ExceptionHandler(GeneralException.class)
	public ResponseEntity<StandardError> generalException(GeneralException e, HttpServletRequest request){
		String error = "Bad request.";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), 
				(e.getCause() != null? e.getCause().getMessage() : null), request.getRequestURI(), 
				(e.getClassObject() != null? e.getClassObject().getSimpleName() : null), 
				(e.getAttributeName()));
		
		SystemActivity systemActivity = SystemActivity.builder()
				.path(request.getRequestURI())
				.operation(request.getMethod())
				.description("Operação não concluída.")
				.statusCode(status.value())
				.errorMessage(e.getLocalizedMessage())
				.stackTrace(e.getMessage())
				.timestamp(Instant.now())
				.build();
				
			systemService.insert(systemActivity);
	
		return ResponseEntity.status(status).body(err);
	}
}
