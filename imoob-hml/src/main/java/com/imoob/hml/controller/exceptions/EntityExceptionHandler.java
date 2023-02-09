package com.imoob.hml.controller.exceptions;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.imoob.hml.model.exceptions.StandardError;
import com.imoob.hml.model.exceptions.ValidationException;
import com.imoob.hml.service.SystemActivityService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class EntityExceptionHandler {
	
	private final SystemActivityService systemService;


	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<StandardError> messageNotReadable(HttpMessageNotReadableException e,
			HttpServletRequest request) throws JsonProcessingException {
		String error = "Há campo(s) não preenchido(s) corretamente.";
		HttpStatus status = HttpStatus.BAD_REQUEST;

		Throwable rootCause = e.getRootCause();

		String attributeName = null;
		if (rootCause instanceof InvalidFormatException) {
			InvalidFormatException invalidFormatException = (InvalidFormatException) rootCause;

			attributeName = getAttributeName(invalidFormatException.getPathReference());
//	            StringBuilder sb = new StringBuilder();
//	            
//	            invalidFormatException.getPathReference(sb);
//	            
//	            String value = invalidFormatException.getLocation().toString();
//	            
//	            System.out.println("Tipo inválido para o atributo " + invalidFormatException.getPath().get(0).getFieldName() + 
//	            ": esperado " + targetType + ", encontrado " + value + " na URL: " + request.getRequestURI() + " || " + sb.);
		}

		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI(), (e.getCause() != null ? e.getCause().getMessage() : null), attributeName);
		
		systemService.insertError(error, status, request, e);


		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<StandardError> usernameNotFound(UsernameNotFoundException e, HttpServletRequest request) throws JsonProcessingException {
		String error = "Usuário não encontrado.";
		HttpStatus status = HttpStatus.NOT_FOUND;

		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				e.getCause().getMessage(), request.getRequestURI());

		systemService.insertError(error, status, request, e);

		
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ValidationException> hibernateValidation(ConstraintViolationException e,
			HttpServletRequest request) throws JsonProcessingException {
//		String error = "Campos com os dados incorretos. [" + e.getConstraintViolations();
		HttpStatus status = HttpStatus.BAD_REQUEST;

		List<Map<String, String>> listErrors = e.getConstraintViolations().stream()
				.map(x -> new HashMap<String, String>() {
					private static final long serialVersionUID = 1L;
					{
						put("attributeName", x.getPropertyPath().toString().toUpperCase());
						put("error", x.getMessage());
					}
				}).collect(Collectors.toList());

		ValidationException err = new ValidationException(Instant.now(), status.value(), request.getRequestURI(),
				listErrors);
		
		systemService.insertError(null, status, request, e);


		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<StandardError> nullPointer(NullPointerException e, HttpServletRequest request) throws JsonProcessingException {
		String error = "Valor nulo.";
		HttpStatus status = HttpStatus.BAD_REQUEST;

		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				(e.getCause() != null ? e.getCause().getMessage() : null), request.getRequestURI());
		
		systemService.insertError(error, status, request, e);


		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<StandardError> usernameNotFound(MethodArgumentTypeMismatchException e, HttpServletRequest request) throws JsonProcessingException {
		String error = "Há parâmetros da requisição sendo passados incorretamente.";
		HttpStatus status = HttpStatus.NOT_FOUND;

		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				e.getCause().getMessage(), request.getRequestURI());

		systemService.insertError(error, status, request, e);

		
		return ResponseEntity.status(status).body(err);
	}
	
	
	/**
	 * Get the attribute name
	 * 
	 * @param pathReference
	 * @return
	 */
	private String getAttributeName(String pathReference) {

		Pattern pattern = Pattern.compile("\".*\"");
		Matcher matcher = pattern.matcher(pathReference);

		if (matcher.find()) {
			return matcher.group().replace("\"", "");
		}
		return null;
	}
}
