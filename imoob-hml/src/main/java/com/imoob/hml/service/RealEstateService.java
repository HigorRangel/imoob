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

	private ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build()
			.setSerializationInclusion(JsonInclude.Include.NON_NULL);

	public RealEstate insert(RealEstate realEstate) {
		validateRealEstateFields(realEstate);
		validateDuplicateRealEstate(realEstate);

		realEstate.setStatus(RealEstateStatus.ACTIVE);
		realEstate.setCreated(Instant.now());
		RealEstate realEstateSaved = repository.save(realEstate);
		return realEstateSaved;
	}

	public List<RealEstate> findAll(Pageable pageable) {
		List<RealEstate> realEstates = repository.findAll(pageable);
		;
		return realEstates;
	}

	public RealEstate findById(Long id) {
		Optional<RealEstate> optRealEstate = repository.findById(id);
		return optRealEstate.orElseThrow(() -> new ResourceNotFoundException(id, RealEstate.class));
	}

	public RealEstate update(Long id, RealEstate realEstate) {
		validateRealEstateFields(realEstate);

		try {
			realEstate.setId(id);
			RealEstate entity = repository.findById(id).get();

			validateDuplicateRealEstate(realEstate, entity.getId());

			realEstate.setCreated(entity.getCreated());
			RealEstate updatedRealEstate = repository.save(realEstate);
			
			return updatedRealEstate;
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException(id, RealEstate.class);
		}
	}

	public RealEstate patchUpdate(Long id, JsonPatch patch) {

		RealEstate realEstatePatched = null;
		try {
			RealEstate currentRealEstate = repository.findById(id).get();
			
			realEstatePatched = applyPatchToUser(patch, currentRealEstate);
			
			validateDuplicateRealEstate(realEstatePatched, currentRealEstate.getId());


			return repository.save(realEstatePatched);
		} catch (JsonPatchException | JsonProcessingException e) {
			throw new GeneralException("Não foi possível atualizar o registro. Verifique as informações inseridas.", e,
					RealEstate.class);
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
		validateWebsite(realEstate.getWebsite());
	}

	private void validateRealEstateNames(RealEstate realEstate) {
		if (StringUtils.isNullOrEmpty(realEstate.getCorporateName())) {
			throw new GeneralException("O campo Razão Social não pode ficar vazio.", RealEstate.class);
		}
		if (StringUtils.isNullOrEmpty(realEstate.getTradingName())) {
			throw new GeneralException("O campo Razão Social não pode ficar vazio.", RealEstate.class);
		}
	}

	private void validateWebsite(String website) {
		if (StringUtils.isNullOrEmpty(website)) {
			throw new GeneralException("O campo website não pode ficar vazio.", RealEstate.class);
		}
		if (!GeneralUtils.isValidURL(website)) {
			throw new GeneralException("O website não é valido.", RealEstate.class);
		}
	}
	
	private void validateDuplicateRealEstate(RealEstate realEstate) {
		RealEstate realEstateOld = repository.findByCnpj(realEstate.getCnpj());
		realEstate = (realEstateOld == null ? repository.findByCnpj(realEstate.getCorporateName()) : realEstateOld);
		
		if(realEstateOld != null) {
			throw new GeneralException("Já existe uma imobiliária com os dados cadastrados.");
		}
	}
	
	
	private void validateDuplicateRealEstate(RealEstate realEstate, Long id) {
		RealEstate realEstateOld = repository.findByCnpj(realEstate.getCnpj());
		realEstate = (realEstateOld == null ? repository.findByCnpj(realEstate.getCorporateName()) : realEstateOld);
		
		if(realEstateOld != null && !realEstate.getId().equals(id)) {
			throw new GeneralException("Já existe uma imobiliária com os dados cadastrados.");
		}
	}

}
