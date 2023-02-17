package com.imoob.hml.model.DTO.route;

import com.imoob.hml.model.Route;
import com.imoob.hml.model.SystemClass;
import com.imoob.hml.model.enums.ApiOperation;

import lombok.Getter;

@Getter
public class RouteDTO {

	private Long id;
	private String route;
	private String operation;

	public RouteDTO(Route route) {
		super();
		this.id = route.getId();
		this.route = route.getRoute();
		this.operation = route.getOperation().getName();
	}

	public ApiOperation getOperation() {
		return ApiOperation.getByName(operation);
	}

	public void setOperation(ApiOperation operation) {
		this.operation = operation.getName();
	}
}
