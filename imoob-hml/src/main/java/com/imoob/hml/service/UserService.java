package com.imoob.hml.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.imoob.hml.model.Permission;
import com.imoob.hml.model.User;
import com.imoob.hml.model.UserPermission;
import com.imoob.hml.model.DTO.usuario.UserPermissionAssignmentDTO;
import com.imoob.hml.model.enums.UserStatus;
import com.imoob.hml.repository.UserPermissionRepository;
import com.imoob.hml.repository.UserRepository;
import com.imoob.hml.service.exceptions.DatabaseException;
import com.imoob.hml.service.exceptions.GeneralException;
import com.imoob.hml.service.exceptions.ResourceNotFoundException;
import com.imoob.hml.service.utils.PersonalDataUtils;
import com.imoob.hml.service.utils.StringUtils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository repository;
	
	private final PermissionService permissionService;
	
	private final UserPermissionRepository userPermissionRepository;
	
	private final ObjectMapper objectMapper;;

	private final PasswordEncoder passwordEncoder;

	public List<User> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}
	
	public List<User> findAllByRealEstate(Long id, Pageable pageable) {
		return repository.findAllByRealEstate(id, pageable);
	}

	public User findUserById(Long id) {
		Optional<User> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id, User.class));
	}

	public User insert(User obj) {
		validateEmptyFields(obj);
		validateDuplicatedUser(obj);
		obj.setStatus(UserStatus.PENDING);
		obj.setCreated(Instant.now());
		obj.setLastUpdate(Instant.now());
		return repository.save(obj);
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id, User.class);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	public User update(Long id, User obj) {
		try {
			validateEmptyFields(obj);
			User entity = repository.findById(id).get();
			validateDuplicatedUserUpdated(entity, obj);
			updateData(entity, obj);
			return repository.save(entity);
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException(id, User.class);
		}
	}

	private void updateData(User entity, User obj) {
		entity.setFirstName(obj.getFirstName());
		entity.setMiddleNames(obj.getMiddleNames());
		entity.setLastName(obj.getLastName());
		entity.setEmail(obj.getEmail());
		entity.setStatus(obj.getStatus());
		entity.setCpf(obj.getCpf());
		entity.setCepAddress(obj.getCepAddress());
		entity.setNumberAddress(obj.getNumberAddress());
		entity.setBirthDate(obj.getBirthDate());
		entity.setLastUpdate(Instant.now());

	}

	public User patchUpdate(Long id, JsonPatch patch) {
		try {
			User currentUser = repository.findById(id).get();
			User userPatched = applyPatchToUser(patch, currentUser);

			validatePatchAttributes(userPatched, currentUser);

			return repository.save(userPatched);
		} catch (JsonPatchException | JsonProcessingException e) {
			throw new GeneralException("Não foi possível atualizar o registro. Verifique as informações inseridas.", e,
					User.class);
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException(id, User.class);
		}
	}

	private void validatePatchAttributes(User userPatched, User currentUser) {

		if (!StringUtils.isNullOrEmpty(userPatched.getFirstName())) {
			validateSpaceBetweenNames(userPatched.getFirstName().trim(), "Primeiro Nome");
		}

		if (!StringUtils.isNullOrEmpty(userPatched.getMiddleNames())) {
			validateSpaceBetweenNames(userPatched.getFirstName().trim(), "Nomes do meio");
		}
		if (!StringUtils.isNullOrEmpty(userPatched.getLastName())) {
			validateSpaceBetweenNames(userPatched.getLastName().trim(), "Último Nome");
		}
		if (!StringUtils.isNullOrEmpty(userPatched.getEmail())) {
			validateEmail(userPatched.getEmail());
			validateDuplicatedUserUpdated(currentUser, userPatched);
		}
		if (userPatched.getStatus() != null) {
			validateUserStatus(userPatched.getStatus());
		}

		if (userPatched.getCpf() != null) {
			validateDuplicatedUserUpdated(currentUser, userPatched);
		}

		if (userPatched.getBirthDate() != null) {
			validateBirthDate(userPatched.getBirthDate());
		}

		if (userPatched.getPassword() != null && !userPatched.getPassword().startsWith("$2a$")) {
			validatePassword(userPatched.getPassword());
			userPatched.setPassword(passwordEncoder.encode(userPatched.getPassword()));
		}

	}

	/**
	 * Validates that the user's password is alphanumeric, has between 8 and 16
	 * characters and has at least one symbol.
	 * 
	 * @param password
	 */
	private void validatePassword(String password) {
		Pattern pattern = Pattern
				.compile("^(?=.*[A-Z])(?=.*[a-zA-Z])(?=.*[-:;@$!%*#<>?&{}\\[\\]\\?\\~_±^¨`´=+\\\\-\\\\/\\']).{8,16}$");
		Matcher matcher = pattern.matcher(password);

		if (!matcher.matches()) {
			throw new GeneralException(
					"Senha inválida. Certifique-se que a senha tenha pelo menos uma letra maiúscula, um número e um símbolo. A senha deve conter entre 8 e 16 caracteres.");
		}
	}

	/**
	 * Validates if user is over 16 years old
	 * 
	 * @param birthDate
	 */
	private void validateBirthDate(LocalDate birthDate) {
		LocalDate today = LocalDate.now();

		Period diff = Period.between(birthDate, today);

		if (diff.getYears() < 16) {
			throw new GeneralException("O usuário deve ter mais de 16 anos.", User.class);
		}
	}

//	
//	private void validatePatch(JsonPatch patch, User userPatched) {
//		if(patch.)
//		
//	}

	private User applyPatchToUser(JsonPatch patch, User targetUser)
			throws JsonProcessingException, IllegalArgumentException, JsonPatchException {
		JsonNode patched = patch.apply(objectMapper.convertValue(targetUser, JsonNode.class));
		return objectMapper.treeToValue(patched, User.class);
	}

//	public User patchUpdate(Long id, User obj) {
//		try {
//			User currentUser = repository.findById(id).get();
//
//			if (!StringUtils.isNullOrEmpty(obj.getFirstName())) {
//				validateSpaceBetweenNames(obj.getFirstName().trim(), "Primeiro Nome");
//				currentUser.setFirstName(obj.getFirstName().trim());
//			}
//
//			if (!StringUtils.isNullOrEmpty(obj.getMiddleNames())) {
//				validateSpaceBetweenNames(obj.getFirstName().trim(), "Primeiro Nome");
//				currentUser.setMiddleNames(obj.getMiddleNames());
//			}
//			if (!StringUtils.isNullOrEmpty(obj.getLastName())) {
//				validateSpaceBetweenNames(obj.getLastName().trim(), "Último Nome");
//				currentUser.setLastName(obj.getLastName().trim());
//			}
//			if (!StringUtils.isNullOrEmpty(obj.getEmail())) {
//				validateEmail(obj.getEmail());
//				validateDuplicatedUserUpdated(currentUser, obj);
//				currentUser.setEmail(obj.getEmail().trim());
//			}
//			if (obj.getStatus() != null) {
//				validateUserStatus(obj.getStatus());
//				currentUser.setStatus(obj.getStatus());
//			}
//
//			if (obj.getCpf() != null) {
//				obj.setCpf(fixCpf(obj.getCpf()));
//				validateDuplicatedUserUpdated(currentUser, obj);
//				currentUser.setCpf(obj.getCpf());
//			}
//
//			if (obj.getCepAddress() != null) {
//
//			}
//
//			return repository.save(currentUser);
//
//		} catch (NoSuchElementException e) {
//			throw new ResourceNotFoundException(id, User.class);
//		}
//	}

	/**
	 * Validate if user Status
	 * 
	 * @param status
	 */
	private void validateUserStatus(UserStatus status) {
		// TODO Auto-generated method stub

	}

	/**
	 * Validates the email
	 * 
	 * @param email
	 */
	private void validateEmail(@NonNull String email) {
		if (!PersonalDataUtils.isValidEmailAddress(email)) {
			throw new GeneralException("O e-mail é inválido.");
		}
	}

	/**
	 * Validates if there are spaces between the names
	 * 
	 * @param name
	 * @param fieldName
	 */
	private void validateSpaceBetweenNames(String name, String fieldName) {
		if (name.contains(" ")) {
			throw new GeneralException("Não é permitido espaços no campo [" + fieldName
					+ "]. (Caso haja mais nomes/sobrenomes, insira nos nomes do meio.)");
		}
	}

	/**
	 * Validates if there are empty fields
	 * 
	 * @param user
	 */
	private void validateEmptyFields(User user) {
		if (StringUtils.isNullOrEmpty(user.getFirstName())) {
			throw new GeneralException("Campo Primeiro Nome não preenchido.");
		}
		if (StringUtils.isNullOrEmpty(user.getLastName())) {
			throw new GeneralException("Campo Último Nome não preenchido.");
		}
		if (StringUtils.isNullOrEmpty(user.getEmail())) {
			throw new GeneralException("Campo Email não preenchido.");
		}
		if (StringUtils.isNullOrEmpty(user.getEmail())) {
			throw new GeneralException("Campo Email não preenchido.");
		}
		if (StringUtils.isNullOrEmpty(user.getCpf())) {
			throw new GeneralException("Campo CPF não preenchido.");
		}
	}

	/**
	 * Validates if there are already users with the data provided
	 * 
	 * @param user
	 */
	private void validateDuplicatedUser(User user) {
		if (repository.findByCpf(user.getCpf()) != null
				|| repository.findByEmail(user.getEmail()).orElseGet(() -> null) != null) {
			throw new DatabaseException("Já existe um registro com os dados informados.");
		}
	}

	/**
	 * Validates if there are already users with the data provided on update
	 * 
	 * @param user
	 */
	private void validateDuplicatedUserUpdated(User oldUser, User newUser) {
		User userByCPF = repository.findByCpf(newUser.getCpf());
		Optional<User> userByEmail = repository.findByEmail(newUser.getEmail());

		if ((userByCPF != null && !userByCPF.getCpf().equals(oldUser.getCpf()))
				|| (userByEmail.orElseGet(() -> null) != null
						&& !userByEmail.get().getEmail().equals(oldUser.getEmail()))) {
			throw new DatabaseException("Já existe um registro com os dados informados.");
		}
	}

	public User assignPermission(UserPermissionAssignmentDTO obj) {
		User user = findUserById(obj.getUser());
		Permission permission = permissionService.findById(obj.getPermission());
		
		validateAssignPermission(user, permission);
		
		UserPermission up = new UserPermission(user, permission, Instant.now());
		up = userPermissionRepository.save(up);
		
		Set<UserPermission> listPermissions = user.getAllPermissions();
		listPermissions.add(up);
		user.setPermissions(listPermissions);
//		user.getPermissions().add(permission);
		return user;
		
	}

	/**
	 * Validates if user already has the permission
	 * @param user
	 * @param permission
	 */
	private void validateAssignPermission(User user, Permission permission) {
		if(user.getPermissions().contains(permission)) {
			throw new GeneralException("A permissão solicitada já está atribuída ao usuário.");
		}
	}

	public User unassignPermission(UserPermissionAssignmentDTO obj) {
		User user = findUserById(obj.getUser());
		Permission permission = permissionService.findById(obj.getPermission());
		
		
		UserPermission up = userPermissionRepository.findByUserPermission(user.getId(), permission.getId());
		
		if(up == null) {
			throw new GeneralException("A permissão não está atribuída à esse usuário.", User.class);
		}
		userPermissionRepository.delete(up);
		
		Set<UserPermission> listPermissions = user.getAllPermissions();
		listPermissions.remove(up);
		user.setPermissions(listPermissions);
		
		return user;
		
	}

}
