package com.imoob.hml.service.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import com.imoob.hml.model.enums.ApiOperation;
import com.imoob.hml.service.RouteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RequestUtils {


	public static boolean isRouteValid(String route, ApiOperation operation, RouteService routeService) {
		if(routeService.findByRouteOperation(route, operation) != null) {
			return false;
		}
		return true;
	}
	
	
	public static boolean isRouteValid(String route, String operation, RouteService routeService) {
		return isRouteValid(route, ApiOperation.getByName(operation), routeService);
	}

}
