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

import com.github.fge.jsonpatch.JsonPatch;
import com.imoob.hml.model.RealEstate;
import com.imoob.hml.model.User;
import com.imoob.hml.service.RealEstateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/realestate")
@RequiredArgsConstructor
public class RealEstateController {
	
	private final RealEstateService service;
	
	@GetMapping("/")
	public ResponseEntity<List<RealEstate>> findAll(Pageable pageable){
		List<RealEstate> list = service.findAll(pageable);
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<RealEstate> findById(@PathVariable Long id){
		RealEstate permission = service.findById(id);
		return ResponseEntity.ok().body(permission);
	}
	
	@PostMapping("/")
	public ResponseEntity<RealEstate> insert(@RequestBody RealEstate realEstate){
		realEstate = service.insert(realEstate);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(realEstate.getId()).toUri();
		
		return ResponseEntity.created(uri).body(realEstate); 
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<RealEstate> update(@PathVariable Long id, @RequestBody RealEstate realEstate){
		realEstate = service.update(id, realEstate);
		return ResponseEntity.ok().body(realEstate);
	}
	
	@PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
	public ResponseEntity<RealEstate> patchUpdate(@PathVariable Long id, @RequestBody JsonPatch patch){
		RealEstate realEstate = service.patchUpdate(id, patch);
			return ResponseEntity.ok(realEstate);
	}
}
