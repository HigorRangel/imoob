package com.imoob.hml.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.imoob.hml.model.Grouping;

@Repository
public interface GroupingRepository extends CrudRepository<Grouping, Long> {

	Grouping findByName(String name);
	
	@Query("SELECT g FROM Grouping g WHERE lower(g.name) = lower(:name)")
    Grouping findByNameIgnoreCase(@Param("name") String name);
}
