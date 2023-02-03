package com.imoob.hml.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.imoob.hml.model.Permission;
import com.imoob.hml.model.RealEstate;
import com.imoob.hml.model.enums.RealStateStatus;
import com.imoob.hml.repository.RealEstateRepository;
import com.imoob.hml.service.exceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RealEstateService {

	private final RealEstateRepository repository;
	
	
	public RealEstate insert(RealEstate realEstate) {
		realEstate.setStatus(RealStateStatus.ACTIVE);
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
}
