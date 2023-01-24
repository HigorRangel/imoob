package com.imoob.hml.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.imoob.hml.model.Role;
@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
	
	
	List<Role> findAll(Pageable pageable);
	
	Role findByName(String name);
	
	Role findByDisplayName(String displayName);
}
