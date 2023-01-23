package com.imoob.hml.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.imoob.hml.model.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
	
	
	List<Role> findAll(Pageable pageable);
}
