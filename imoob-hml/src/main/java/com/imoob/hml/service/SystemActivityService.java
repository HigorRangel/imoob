package com.imoob.hml.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.imoob.hml.config.JwtService;
import com.imoob.hml.model.SystemActivity;
import com.imoob.hml.model.User;
import com.imoob.hml.repository.SystemActivityRepository;
import com.imoob.hml.service.exceptions.GeneralException;
import com.imoob.hml.service.utils.GeneralUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SystemActivityService {
	private final SystemActivityRepository systemActivityRepository;

	private final JwtService jwtService;

	public SystemActivity insert(SystemActivity systemActivity) {
		return systemActivityRepository.save(systemActivity);
	}

	public void insertOk(String path, Long id, HttpServletRequest request, Object object) {

		User user = jwtService.getUserByAuthorization(request.getHeader("Authorization"));
		
		String bodyJson = null;
		try {
			bodyJson = GeneralUtils.convertObjectToJson(object);
		} catch (JsonProcessingException e) {
			throw new GeneralException("Não foi possível insrir o registro. Verifique as informações inseridas.", e,
					object);
		}

		SystemActivity systemActivity = SystemActivity.builder().path(path).objectId(id).operation(request.getMethod())
				.description("Operação concluída.").statusCode(HttpStatus.OK.value()).timestamp(Instant.now())
				.user(user).body(bodyJson).build();

		this.insert(systemActivity);
	}

	public void insertError(String path, Long id, HttpServletRequest request) {

		User user = jwtService.getUserByAuthorization(request.getHeader("Authorization"));

		SystemActivity systemActivity = SystemActivity.builder().path(path).objectId(id).operation(request.getMethod())
				.description("Operação concluída.").statusCode(HttpStatus.OK.value()).timestamp(Instant.now())
				.user(user).build();

		this.insert(systemActivity);
	}

	public void insertError(String error, Long objectId, HttpStatus status, HttpServletRequest request,
			GeneralException e, Object body) throws JsonProcessingException {
		
		User user = jwtService.getUserByAuthorization(request.getHeader("Authorization"));
		

		SystemActivity systemActivity = SystemActivity.builder()
				.path(request.getRequestURI())
				.operation(request.getMethod())
				.description("Operação não concluída.")
				.statusCode(status.value())
				.errorMessage(e.getLocalizedMessage())
				.stackTrace(e.getMessage())
				.timestamp(Instant.now())
				.user(user)
				.body(GeneralUtils.convertObjectToJson(body))
				.build();
		
		this.insert(systemActivity);
		
	}
}
