package com.imoob.hml.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.imoob.hml.config.JwtService;
import com.imoob.hml.model.SystemActivity;
import com.imoob.hml.model.User;
import com.imoob.hml.repository.SystemActivityRepository;
import com.imoob.hml.service.exceptions.GeneralException;
import com.imoob.hml.service.utils.GeneralUtils;
import com.imoob.hml.service.utils.StringUtils;

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

	public void insertOk(String path, Long id, HttpServletRequest request) {

		User user = jwtService.getUserByAuthorization(request.getHeader("Authorization"));
		

		SystemActivity systemActivity = SystemActivity.builder().path(path).objectId(id).operation(request.getMethod())
				.description("Operação concluída.").statusCode(HttpStatus.OK.value()).timestamp(Instant.now())
				.user(user).build();

		this.insert(systemActivity);
	}

	public void insertError(String path, Long id, HttpServletRequest request) {

		User user = jwtService.getUserByAuthorization(request.getHeader("Authorization"));

		SystemActivity systemActivity = SystemActivity.builder().path(path).objectId(id).operation(request.getMethod())
				.description("Operação concluída.").statusCode(HttpStatus.OK.value()).timestamp(Instant.now())
				.user(user).build();

		this.insert(systemActivity);
	}

	public void insertError(String error, HttpStatus status, HttpServletRequest request,
			Throwable e) throws JsonProcessingException {
		
		String strId = getPathId(request);
		
		User user = jwtService.getUserByAuthorization(request.getHeader("Authorization"));
		

		SystemActivity systemActivity = SystemActivity.builder()
				.path(request.getRequestURI())
				.operation(request.getMethod())
				.description("Operação não concluída.")
				.statusCode(status.value())
				.errorMessage(e.getLocalizedMessage())
				.objectId((strId != null ? Long.valueOf(strId) : null))
				.stackTrace(e.getMessage())
				.timestamp(Instant.now())
				.user(user)
				.build();
		
		this.insert(systemActivity);
		
	}

	private String getPathId(HttpServletRequest request) {
		String path = request.getRequestURI();
		String id = null;
		if (path != null) {
		  List<String> parts = Arrays.asList(path.split("/"));
		  if(parts.size() == 0) {
			  return null;
		  }
		  parts = parts.stream().filter((param) -> StringUtils.isOnlyNumbers(param)).toList();
		  id = (parts.size() != 0 ? parts.get(parts.size() - 1) : null);
		}
		return id;
	}
}
