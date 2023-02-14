package com.imoob.hml.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.imoob.hml.model.Route;
import com.imoob.hml.model.enums.ApiOperation;

public interface RouteRepository extends CrudRepository<Route, Long>{

	@Query("select r from Route r where lower(r.route) = lower(:route) and r.operation = :operation")
	Route findByRouteOperation(@Param("route") String route, @Param("operation") ApiOperation operation);

}
