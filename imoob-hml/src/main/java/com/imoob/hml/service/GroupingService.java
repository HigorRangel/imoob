	package com.imoob.hml.service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.imoob.hml.model.Grouping;
import com.imoob.hml.model.Permission;
import com.imoob.hml.model.User;
import com.imoob.hml.model.DTO.usuario.GroupingPermissionDTO;
import com.imoob.hml.repository.GroupingRepository;
import com.imoob.hml.repository.PermissionRepository;
import com.imoob.hml.service.exceptions.GeneralException;
import com.imoob.hml.service.exceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupingService {
	private final GroupingRepository groupingRepository;
	
	private final PermissionService permissionService;
	
	private ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build()
			.setSerializationInclusion(JsonInclude.Include.NON_NULL);

	public Grouping insertGrouping(GroupingPermissionDTO groupingPermission) {
		
		if(groupingPermission.getPermissions() == null || groupingPermission.getPermissions().size() == 0) {
			throw new GeneralException("A lista de permissões não pode ser vazia.", Grouping.class);
		}
		
		validateDuplicateGroupingName(groupingPermission.getName());
		
		Grouping grouping = Grouping.builder()
				.name(groupingPermission.getName())
				.description(groupingPermission.getDescription())
				.enabled(true).build();
		
		Set<Permission> permissions = getPermissions(groupingPermission.getPermissions());
		
		
		grouping.setPermissions(permissions);
		return groupingRepository.save(grouping);
		
	}

	private void validateDuplicateGroupingName(String name) {
		Grouping grouping = groupingRepository.findByNameIgnoreCase(name);
		if(grouping != null) {
			throw new GeneralException("Já existe um agrupamento com o nome '" + name + "'");
		}
	}

	private Set<Permission> getPermissions(Set<Long> permissions) {
		return permissions.stream().map(id -> permissionService.findById(id)).collect(Collectors.toSet());
	}

	public Grouping patchUpdate(Long id, JsonPatch patch) {
		try {
			Grouping currentGrouping = groupingRepository.findById(id).get();
			GroupingPermissionDTO groupingPatched = applyPatchToUser(patch, new GroupingPermissionDTO(currentGrouping, null));

			Set<Permission> permissions = new HashSet<Permission>();
			
			permissions.addAll(getPermissions(groupingPatched.getPermissions()));

			
			Grouping newGrouping = new Grouping(null, groupingPatched.getName(), groupingPatched.getDescription(), groupingPatched.getEnabled(), permissions);
				
			return groupingRepository.save(newGrouping);
		} catch (JsonPatchException | JsonProcessingException e) {
			throw new GeneralException("Não foi possível atualizar o registro. Verifique as informações inseridas.", e,
					Grouping.class);
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException(id, User.class);
		}
	}
	


	private GroupingPermissionDTO applyPatchToUser(JsonPatch patch, GroupingPermissionDTO targetGrouping)
			throws JsonProcessingException, IllegalArgumentException, JsonPatchException {
		JsonNode patched = patch.apply(objectMapper.convertValue(targetGrouping, JsonNode.class));
		return objectMapper.treeToValue(patched, GroupingPermissionDTO.class);
	}
	
	
	
}
