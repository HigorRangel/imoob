package com.imoob.hml.service;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.imoob.hml.config.JwtService;
import com.imoob.hml.model.SystemActivity;
import com.imoob.hml.repository.SystemActivityRepository;

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
	
		SystemActivity systemActivity = SystemActivity.builder()
				.path(path)
				.objectId(id)
				.operation(request.getMethod())
				.description("Operação concluída.")
				.statusCode(HttpStatus.OK.value())
				.timestamp(Instant.now())
				.build();
		
		this.insert(systemActivity);
	}
}
