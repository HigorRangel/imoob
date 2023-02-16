package com.imoob.hml.service;

import org.springframework.stereotype.Service;

import com.imoob.hml.model.Route;
import com.imoob.hml.model.enums.ApiOperation;
import com.imoob.hml.repository.RouteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteService {

	private final RouteRepository routeRepository;
	
	public Route findByRouteOperation(String route, ApiOperation operation) {
		return routeRepository.findByRouteOperation(route, operation.getName());
	}

	public Route insert(Route route) {
		return routeRepository.save(route);
	}
}
