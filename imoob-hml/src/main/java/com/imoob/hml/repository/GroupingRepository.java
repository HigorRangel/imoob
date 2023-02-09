package com.imoob.hml.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.imoob.hml.model.Grouping;

@Repository
public interface GroupingRepository extends CrudRepository<Grouping, Long> {

	Grouping findByName(String name);
}
