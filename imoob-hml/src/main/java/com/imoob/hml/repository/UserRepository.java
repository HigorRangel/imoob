package com.imoob.hml.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.imoob.hml.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	Optional<User> findByEmail(String email);

	List<User> findAll(Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.realEstate.id = :realEstateId")
	List<User> findAllByRealEstate(@Param("realEstateId") Long realEstateId, Pageable pageable);

	User findByCpf(String cpf);

}
