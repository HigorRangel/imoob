package com.imoob.hml.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.imoob.hml.model.RealEstate;
import com.imoob.hml.model.Role;
import com.imoob.hml.model.User;
import com.imoob.hml.repository.RealEstateRepository;
import com.imoob.hml.service.exceptions.DatabaseException;
import com.imoob.hml.service.exceptions.GeneralException;
import com.imoob.hml.service.exceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RealEstateService {

	private final RealEstateRepository realEstateRepository;

	private ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build()
			.setSerializationInclusion(JsonInclude.Include.NON_NULL);

	public List<RealEstate> findAll(Pageable pageable) {
		return realEstateRepository.findAll(pageable);
	}

	public RealEstate findById(Long id) {
		return realEstateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id, RealEstate.class));
	}

	public RealEstate save(RealEstate realEstate) {
		return realEstateRepository.save(realEstate);
	}

	public RealEstate update(Long id, RealEstate realEstate) {
		try {
			RealEstate existingRealEstate = findById(id);
			BeanUtils.copyProperties(realEstate, existingRealEstate, "id");
			return realEstateRepository.save(existingRealEstate);
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException(id, RealEstate.class);
		}
	}

	public void deleteById(Long id) {
		realEstateRepository.deleteById(id);
		try {
			realEstateRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id, RealEstate.class);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	public RealEstate patchUpdate(Long id, JsonPatch patch) {
		try {
			RealEstate currentUser = realEstateRepository.findById(id).get();
			RealEstate userPatched = applyPatchToUser(patch, currentUser);

//			validatePatchAttributes(userPatched, currentUser);

			return realEstateRepository.save(userPatched);
		} catch (JsonPatchException | JsonProcessingException e) {
			throw new GeneralException("Não foi possível atualizar o registro. Verifique as informações inseridas.", e,
					User.class);
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException(id, User.class);
		}
	}

	private RealEstate applyPatchToUser(JsonPatch patch, RealEstate targetRealEstate)
			throws JsonProcessingException, IllegalArgumentException, JsonPatchException {
		JsonNode patched = patch.apply(objectMapper.convertValue(targetRealEstate, JsonNode.class));
		return objectMapper.treeToValue(patched, RealEstate.class);
	}
}
