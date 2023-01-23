package com.imoob.hml.service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.imoob.hml.model.User;
import com.imoob.hml.model.enums.UserStatus;
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
	
	public List<User> findAll(Pageable pageable){
		return repository.findAll(pageable);
	}
	
	public User findUserById(Long id) {
		Optional<User> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id, User.class));
	}
	
	public User insert (User obj) {
		return repository.save(obj);
	}
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id, User.class);
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	public User update(Long id, User obj) {
		try {
			User entity = repository.findById(id).get();
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

	public User patchUpdate(Long id, User obj) {
		try {
			User currentUser = repository.findById(id).get();
			
			if(!StringUtils.isNullOrEmpty(obj.getFirstName())) {
				validateSpaceBetweenNames(obj.getFirstName().trim(), "Primeiro Nome");
				currentUser.setFirstName(obj.getFirstName().trim());
			}
			
			if(!StringUtils.isNullOrEmpty(obj.getMiddleNames())) {
				validateSpaceBetweenNames(obj.getFirstName().trim(), "Primeiro Nome");
				currentUser.setMiddleNames(obj.getMiddleNames());
			}
			if(!StringUtils.isNullOrEmpty(obj.getLastName())) {
				validateSpaceBetweenNames(obj.getLastName().trim(), "Último Nome");
				currentUser.setLastName(obj.getLastName().trim());
			}
			if(!StringUtils.isNullOrEmpty(obj.getEmail())) {
				validateEmail(obj.getEmail());
				currentUser.setEmail(obj.getEmail().trim());
			}
			if(obj.getStatus() != null) {
				validateUserStatus(obj.getStatus());
				currentUser.setEmail(obj.getEmail().trim());
			}
			
			return repository.save(currentUser);
			
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException(id, User.class);
		}
	}
	
	/**
	 * Validate if user Status
	 * @param status
	 */
	private void validateUserStatus(UserStatus status) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Validates the email
	 * @param email
	 */
	private void validateEmail(@NonNull String email) {
		if(!PersonalDataUtils.isValidEmailAddress(email)) {
			throw new GeneralException("O e-mail é inválido.");
		}
	}

	/**
	 * Validates if there are spaces between the names
	 * @param name
	 * @param fieldName
	 */
	private void validateSpaceBetweenNames(String name, String fieldName) {
		if(name.contains(" ")) {
			throw new GeneralException("Não é permitido espaços no campo [" + fieldName + "]. (Caso haja mais nomes/sobrenomes, insira nos nomes do meio.)");
		}
	}
	
	
}
