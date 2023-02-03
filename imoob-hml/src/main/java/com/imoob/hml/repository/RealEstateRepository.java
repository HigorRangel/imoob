package com.imoob.hml.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.imoob.hml.model.Permission;
import com.imoob.hml.model.RealEstate;

@Repository
public interface RealEstateRepository extends CrudRepository<RealEstate, Long> {
	List<RealEstate> findAll(Pageable pageable);
}
