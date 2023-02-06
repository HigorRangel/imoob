package com.imoob.hml.controller.exceptions;

import java.io.IOException;
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
	public ResponseEntity<StandardError> generalException(GeneralException e, HttpServletRequest request)
			throws IOException {
		String error = "Bad request.";
		HttpStatus status = HttpStatus.BAD_REQUEST;

		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				(e.getCause() != null ? e.getCause().getMessage() : null), request.getRequestURI(),
				(e.getClassObject() != null ? e.getClassObject().getSimpleName() : null), (e.getAttributeName()));

		systemService.insertError(error, null, status, request, e, e.getObject());

		return ResponseEntity.status(status).body(err);
	}
}
