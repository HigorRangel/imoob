package com.imoob.hml.model.DTO.usuario;

import java.util.HashSet;
import java.util.Set;

import com.imoob.hml.model.Grouping;
import com.imoob.hml.service.utils.converters.BooleanConverter;

import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupingPermissionDTO {
	
	private Long id;
	
	private String name;
	
	private String description;
	
	@Convert(converter = BooleanConverter.class)
	private Boolean enabled;
	
	
	Set<Long> permissions = new HashSet<>();

	public GroupingPermissionDTO(Grouping grouping, Set<Long> permissions) {
		super();
		this.name = grouping.getName();
		this.enabled = grouping.getEnabled();
		this.permissions = permissions;
	}
	
}
