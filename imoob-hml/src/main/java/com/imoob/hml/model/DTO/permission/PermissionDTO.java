package com.imoob.hml.model.DTO.permission;

import com.imoob.hml.model.Permission;
import com.imoob.hml.model.DTO.route.RouteDTO;
import com.imoob.hml.service.utils.converters.BooleanConverter;

import jakarta.persistence.Convert;
import lombok.Getter;

@Getter
public class PermissionDTO {
	
	private Long id;
	
	private String name;
	
	private String displayName;

	private String description;
	
	@Convert(converter = BooleanConverter.class)
	private Boolean enabled;
	
	private RouteDTO route;
	

	public PermissionDTO(Permission permission) {
		super();
		this.id = permission.getId();
		this.name = permission.getName();
		this.displayName = permission.getDisplayName();
		this.description = permission.getDescription();
		this.enabled = permission.getEnabled();
		this.route = new RouteDTO(permission.getRoute());
	}
	
	
	
}
