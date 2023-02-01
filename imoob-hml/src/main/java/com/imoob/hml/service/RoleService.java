package com.imoob.hml.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.imoob.hml.model.Role;
import com.imoob.hml.repository.RoleRepository;
import com.imoob.hml.service.exceptions.DatabaseException;
import com.imoob.hml.service.exceptions.GeneralException;
import com.imoob.hml.service.exceptions.ResourceNotFoundException;
import com.imoob.hml.service.utils.StringUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {
	
	private final RoleRepository roleRepository;
	
	public List<Role> findAll(Pageable pageable){
		return roleRepository.findAll(pageable);
	}
	
	public Role findById(Long id) {
		Optional<Role> userOpt = roleRepository.findById(id);
		return userOpt.orElseThrow(() -> new ResourceNotFoundException(id, Role.class));
	}

	public Role insert(Role role) {
		validateEmptyRoleFields(role);
		validateRoleName(role.getName());
		validateDuplicateRoles(role);
		role.setName(role.getName().toUpperCase());
		return roleRepository.save(role);
	}
	
	public void delete(Long id) {
		try {
			roleRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id, Role.class);
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	public Role update(Long id, Role obj) {
		validateEmptyRoleFields(obj);
		validateRoleName(obj.getName());
		
		try {
			Role entity = roleRepository.findById(id).get();
			validateDuplicateRolesUpdate(obj, entity);
			
			updateData(entity, obj);

			return roleRepository.save(entity);
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException(id, Role.class);
		}
	}

	private void updateData(Role entity, Role obj) {
		entity.setDescription(obj.getDescription());
		entity.setName(obj.getName().toUpperCase());
		entity.setDisplayName(obj.getDisplayName());
	}
	
	public Role patchUpdate(Long id, Role obj) {
		try {
			Role entity = roleRepository.findById(id).get();

			if(!StringUtils.isNullOrEmpty(obj.getName())) {
				validateRoleName(obj.getName().toUpperCase().trim());
				validateDuplicateRolesUpdate(obj, entity);
				entity.setName(obj.getName().toUpperCase().trim());
			}
			if(!StringUtils.isNullOrEmpty(obj.getDescription())) {
				validateDuplicateRolesUpdate(obj, entity);
				entity.setDescription(obj.getDescription().trim());
			}
			if(!StringUtils.isNullOrEmpty(obj.getDisplayName())) {
				entity.setDisplayName(obj.getDisplayName().trim());
			}
			return roleRepository.save(entity);
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException(id, Role.class);
		}
			
	}

	/**
	 * Validates if role name contains space or Diacritical Marks
	 * @param value
	 */
	private void validateRoleName(String value) {
		if(value.contains(" ")) {
			throw new GeneralException("Não é permitido espaços no campo [Nome da Role]. User \"_\" para separar nomes. Ex: \"NOME_DA_ROLE\".");
		}
		if(StringUtils.containsDiacriticalMarks(value)) {
			throw new GeneralException("Não é permitido acentuações no campo [Nome da Role]. Remova-as e tente novamente.");
		}
	}
	
	
	/**
	 * Validates if the fields are empty
	 * @param permission
	 */
	private void validateEmptyRoleFields(Role role) {
		if(StringUtils.isNullOrEmpty(role.getName().trim())) {
			throw new GeneralException("Campo Nome não preenchido.");
		}
		if(StringUtils.isNullOrEmpty(role.getDisplayName().trim())) {
			throw new GeneralException("Campo Nome de Exibição não preenchido.");
		}
	}
	
	/**
	 * Validates if there are already records with the same data
	 * @param permission
	 */
	private void validateDuplicateRoles(Role role) {
		if(roleRepository.findByName(role.getName().trim().toUpperCase()) != null) {
			throw new DatabaseException("Já existe um registro com o nome '" + role.getName().toUpperCase() + "'");
		}
		if(roleRepository.findByDisplayName(role.getDisplayName().trim()) != null) {
			throw new DatabaseException("Já existe um registro com o nome de exibição '" + role.getDisplayName() + "'");
		}
	}
	
	
	/**
	 * Validates if there are already records with the same data
	 * 
	 * @param permissionUpdate
	 * @param oldPermission
	 */
	private void validateDuplicateRolesUpdate(Role newRole, Role oldRole) {
		Role roleByName = roleRepository.findByName(newRole.getName().trim().toUpperCase());
		Role roleByDisplayName = roleRepository.findByDisplayName(newRole.getDisplayName().trim());

		if (roleByName != null && !roleByName.getId().equals(oldRole.getId())) {
			throw new DatabaseException(
					"Já existe um registro com o nome '" + newRole.getName().toUpperCase() + "'");
		}
		if (roleByDisplayName != null && !roleByDisplayName.getId().equals(oldRole.getId())) {
			throw new DatabaseException(
					"Já existe um registro com o nome de exibição '" + newRole.getDisplayName() + "'");
		}
	}
}
