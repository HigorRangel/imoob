package com.imoob.hml.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.imoob.hml.model.Permission;
import com.imoob.hml.model.User;
import com.imoob.hml.repository.PermissionRepository;
import com.imoob.hml.repository.RouteRepository;
import com.imoob.hml.service.exceptions.DatabaseException;
import com.imoob.hml.service.exceptions.GeneralException;
import com.imoob.hml.service.exceptions.ResourceNotFoundException;
import com.imoob.hml.service.utils.RequestUtils;
import com.imoob.hml.service.utils.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermissionService {

	private final PermissionRepository repository;
	
	private final RouteService routeService;

	public List<Permission> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Permission findById(Long id) {
		Optional<Permission> optPermission = repository.findById(id);
		return optPermission.orElseThrow(() -> new ResourceNotFoundException(id, Permission.class));
	}

	public Permission insert(Permission permission, HttpServletRequest request) {
		validateEmptyPermissionFields(permission);
		validatePermissionName(permission.getName());
		validateDuplicatePermissions(permission);
		//TODO Verificar como será feito, já que o path foi passado para a entidade
//		validateUri(permission.getPath(), request);
		permission.setName(permission.getName().toUpperCase());
		permission.setEnabled(true);
		return repository.save(permission);
	}

	private void validateUri(String path, HttpServletRequest request) {
		if(!RequestUtils.isRouteValid(path, request.getMethod(), routeService)) {
			throw new GeneralException("A rota informada não é válida.");
		}
		//TODO Fazer o isRouteValid
		
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id, Permission.class);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	public Permission update(Long id, Permission obj) {
		validateEmptyPermissionFields(obj);
		validatePermissionName(obj.getName());

		try {
			Permission entity = repository.findById(id).get();
			validateDuplicatePermissionsUpdate(obj, entity);
			updateData(entity, obj);
			return repository.save(entity);
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException(id, Permission.class);
		}
	}

	private void updateData(Permission entity, Permission obj) {
		entity.setName(obj.getName().trim().toUpperCase());
		entity.setDisplayName(obj.getDisplayName());
		entity.setDescription(obj.getDescription());
	}

	public Permission patchUpdate(Long id, Permission obj) {
		try {
			Permission entity = repository.findById(id).get();

			if (!StringUtils.isNullOrEmpty(obj.getName())) {
				validatePermissionName(obj.getName().trim());
				entity.setName(obj.getName().trim().toUpperCase());
			}
			if (!StringUtils.isNullOrEmpty(obj.getDisplayName())) {
				entity.setDisplayName(obj.getDisplayName().trim());
			}
			if (!StringUtils.isNullOrEmpty(obj.getDescription())) {
				entity.setDisplayName(obj.getDescription().trim());
			}

			return repository.save(entity);
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException(id, Permission.class);
		}
	}

	public boolean validatePermission(HttpServletRequest request, UserDetails userDetails) {
		if (userDetails instanceof User) {
			User user = (User) userDetails;
			
			
		}

		return false;
	}

	/**
	 * Validates if role name contains space or Diacritical Marks
	 * 
	 * @param value
	 */
	private void validatePermissionName(String value) {
		if (value.contains(" ")) {
			throw new GeneralException(
					"Não é permitido espaços no campo [Nome da Permissão]. User \"_\" para separar nomes. Ex: \"NOME_DA_PERMISSAO\".");
		}
		if (StringUtils.containsDiacriticalMarks(value)) {
			throw new GeneralException(
					"Não é permitido acentuações no campo [Nome da Permissão]. Remova-as e tente novamente.");
		}
	}

	/**
	 * Validates if the fields are empty
	 * 
	 * @param permission
	 */
	private void validateEmptyPermissionFields(Permission permission) {
		if (StringUtils.isNullOrEmpty(permission.getName().trim())) {
			throw new GeneralException("Campo Nome não preenchido.");
		}
		if (StringUtils.isNullOrEmpty(permission.getDisplayName().trim())) {
			throw new GeneralException("Campo Nome de Exibição não preenchido.");
		}
		//TODO Verificar se há a necessidade. Esses parâmetros foram passados para a entidade Route
//		if (StringUtils.isNullOrEmpty(permission.getPath().trim())) {
//			throw new GeneralException("Campo Rota não preenchido.");
//		}
//		if (permission.getOperation() == null) {
//			throw new GeneralException("Campo Operação não preenchido.");
//		}
	}

	/**
	 * Validates if there are already records with the same data
	 * 
	 * @param permission
	 */
	private void validateDuplicatePermissions(Permission permission) {
		if (repository.findByName(permission.getName().trim().toUpperCase()) != null) {
			throw new DatabaseException(
					"Já existe um registro com o nome '" + permission.getName().toUpperCase() + "'");
		}
		if (repository.findByDisplayName(permission.getDisplayName().trim()) != null) {
			throw new DatabaseException(
					"Já existe um registro com o nome de exibição '" + permission.getDisplayName() + "'");
		}
		
		//TODO Verificar a real necessidade. Os atributos foram passados para a entidade route
//		if (repository.findByRoute(permission.getPath().trim(), permission.getOperation().getName()) != null) {
//			throw new DatabaseException("Já existe um registro para a rota informada.");
//		}
	}

	/**
	 * Validates if there are already records with the same data
	 * 
	 * @param permissionUpdate
	 * @param oldPermission
	 */
	private void validateDuplicatePermissionsUpdate(Permission newPermission, Permission oldPermission) {
		Permission permissionByName = repository.findByName(newPermission.getName().trim().toUpperCase());
		Permission permissionByDisplayName = repository.findByDisplayName(newPermission.getDisplayName().trim());

		if (permissionByName != null && !permissionByName.getId().equals(oldPermission.getId())) {
			throw new DatabaseException(
					"Já existe um registro com o nome '" + newPermission.getName().toUpperCase() + "'");
		}
		if (permissionByDisplayName != null && !permissionByDisplayName.getId().equals(oldPermission.getId())) {
			throw new DatabaseException(
					"Já existe um registro com o nome de exibição '" + newPermission.getDisplayName() + "'");
		}
	}

}
