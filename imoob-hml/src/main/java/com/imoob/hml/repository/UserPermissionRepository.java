package com.imoob.hml.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.imoob.hml.model.UserPermission;

public interface UserPermissionRepository extends CrudRepository<UserPermission, Long>{

	@Query("select up from UserPermission up where id.user.id = :userId and id.permission.id = :permissionId")
	UserPermission findByUserPermission(@Param("userId") Long userId, @Param("permissionId") Long permissionId);

}
