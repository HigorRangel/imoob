package com.imoob.hml.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.imoob.hml.model.Permission;

@Repository
public interface PermissionRepository extends CrudRepository<Permission, Long> {
	List<Permission> findAll(Pageable pageable);
	
	Permission findByName(String name);
	
	Permission findByDisplayName(String displayName);
}
