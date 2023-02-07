package com.imoob.hml.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.imoob.hml.model.Grouping;
import com.imoob.hml.model.Permission;
import com.imoob.hml.model.DTO.usuario.GroupingPermissionDTO;
import com.imoob.hml.repository.GroupingRepository;
import com.imoob.hml.repository.PermissionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupingService {
	private final GroupingRepository groupingRepository;
	
	private final PermissionService permissionService;

	public Grouping insertGrouping(GroupingPermissionDTO groupingPermission) {
		Grouping grouping = Grouping.builder()
				.name(groupingPermission.getName())
				.description(groupingPermission.getDescription())
				.enabled(true).build();
		
		Set<Permission> permissions = null;
		
		if(groupingPermission.getPermissions() != null && groupingPermission.getPermissions().size() != 0) {
			permissions = getPermissions(groupingPermission.getPermissions());
		}
		
		grouping.setPermissions(permissions);
		return groupingRepository.save(grouping);
		
	}

	private Set<Permission> getPermissions(Set<Long> permissions) {
		return permissions.stream().map(id -> permissionService.findById(id)).collect(Collectors.toSet());
	}
	
	
	
}
