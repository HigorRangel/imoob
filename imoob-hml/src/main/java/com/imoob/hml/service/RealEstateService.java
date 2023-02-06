package com.imoob.hml.service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.imoob.hml.config.JwtService;
import com.imoob.hml.model.Permission;
import com.imoob.hml.model.RealEstate;
import com.imoob.hml.model.User;
import com.imoob.hml.model.enums.RealEstateStatus;
import com.imoob.hml.repository.RealEstateRepository;
import com.imoob.hml.service.exceptions.GeneralException;
import com.imoob.hml.service.exceptions.ResourceNotFoundException;
import com.imoob.hml.service.utils.GeneralUtils;
import com.imoob.hml.service.utils.StringUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RealEstateService {

	private final RealEstateRepository repository;
	
	
	private ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build().setSerializationInclusion(JsonInclude.Include.NON_NULL);

	
	
	public RealEstate insert(RealEstate realEstate) {
		validateRealEstateFields(realEstate);
		realEstate.setStatus(RealEstateStatus.ACTIVE);
		realEstate.setCreated(Instant.now());
		RealEstate realEstateSaved = repository.save(realEstate);
		return realEstateSaved;
	}
	
	public List<RealEstate> findAll(Pageable pageable){
		List<RealEstate> realEstates = repository.findAll(pageable);;
		return realEstates;
	}
	
	public RealEstate findById(Long id) {
		Optional<RealEstate> optRealEstate = repository.findById(id);
		return optRealEstate.orElseThrow(() -> new ResourceNotFoundException(id, RealEstate.class));
	}

	public RealEstate update(Long id, RealEstate realEstate) {
		
		try {
			realEstate.setId(id);
			RealEstate updatedRealEstate = repository.save(realEstate);
			return updatedRealEstate;
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException(id, RealEstate.class);
		}
	}
	
	public RealEstate patchUpdate(Long id, JsonPatch patch) {
		
		RealEstate userPatched = null;
		try {
			RealEstate currentRealEstate = repository.findById(id).get();
			 userPatched = applyPatchToUser(patch, currentRealEstate);


			return repository.save(userPatched);
		} catch (JsonPatchException | JsonProcessingException e) {
			throw new GeneralException("Não foi possível atualizar o registro. Verifique as informações inseridas.", e,
					userPatched);
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException(id, User.class);
		}
	}
	
	private RealEstate applyPatchToUser(JsonPatch patch, RealEstate targetUser)
			throws JsonProcessingException, IllegalArgumentException, JsonPatchException {
		JsonNode patched = patch.apply(objectMapper.convertValue(targetUser, JsonNode.class));
		return objectMapper.treeToValue(patched, RealEstate.class);
	}
	
	private void validateRealEstateFields(RealEstate realEstate) {
		validateRealEstateNames(realEstate);
		validateWebsite(realEstate);
	}
	
	
	private void validateRealEstateNames(RealEstate realEstate) {
		if(StringUtils.isNullOrEmpty(realEstate.getCorporateName())) {
			throw new GeneralException("O campo Razão Social não pode ficar vazio.", realEstate);
		}
		if(StringUtils.isNullOrEmpty(realEstate.getTradingName())){
			throw new GeneralException("O campo Razão Social não pode ficar vazio.", realEstate);
		}
	}
	
	private void validateWebsite(RealEstate realEstate) {
		if(StringUtils.isNullOrEmpty(realEstate.getWebsite())) {
			throw new GeneralException("O campo website não pode ficar vazio.", realEstate);
		}
		if(!GeneralUtils.isValidURL(realEstate.getWebsite())) {
			throw new GeneralException("O website não é valido.", realEstate);
		}
	}
	
}
