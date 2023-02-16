package com.imoob.hml.controller;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.imoob.hml.model.Route;
import com.imoob.hml.service.RouteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/routes")
@RequiredArgsConstructor
public class RouteController {
	
	private final RouteService service;
	
//	@GetMapping("/")
//	public ResponseEntity<List<Route>> findAll(Pageable pageable){
//		List<Route> list = service.findAll(pageable);
//		return ResponseEntity.ok().body(list);
//	}
//	
//	@GetMapping("/{id}")
//	public ResponseEntity<Route> findById(@PathVariable Long id){
//		Route route = service.findById(id);
//		return ResponseEntity.ok().body(route);
//	}
//	
	@PostMapping("/")
	public ResponseEntity<Route> insert(@RequestBody Route route){
		route = service.insert(route);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(route.getId()).toUri();
		
		return ResponseEntity.created(uri).body(route); 
	}
//	
//	@PutMapping("/{id}")
//	public ResponseEntity<Route> update(@PathVariable Long id, @RequestBody Route route){
//		route = service.update(id, route);
//		return ResponseEntity.ok().body(route);
//	}
//	
//	@PatchMapping("/{id}")
//	public ResponseEntity<Route> patchUpdate(@PathVariable Long id, @RequestBody Route route){
//		route = service.patchUpdate(id, route);
//		return ResponseEntity.ok().body(route);
//	}
}
