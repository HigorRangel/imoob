package com.imoob.hml.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.imoob.hml.model.SystemActivity;

@Repository
public interface SystemActivityRepository extends CrudRepository<SystemActivity, Long>{

}
