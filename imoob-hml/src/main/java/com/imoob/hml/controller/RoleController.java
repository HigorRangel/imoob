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

import com.imoob.hml.model.Role;
import com.imoob.hml.service.RoleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/roles")
@RequiredArgsConstructor
public class RoleController {
	
	private final RoleService service;
	
	@GetMapping
	public ResponseEntity<List<Role>> findAll(Pageable pageable){
		List<Role> list = service.findAll(pageable);
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Role> findById(@PathVariable Long id){
		Role role = service.findById(id);
		return ResponseEntity.ok().body(role);
	}
	
	@PostMapping
	public ResponseEntity<Role> insert(@RequestBody Role role){
		role = service.insert(role);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(role.getId()).toUri();
		
		return ResponseEntity.created(uri).body(role); 
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Role> update(@PathVariable Long id, @RequestBody Role role){
		role = service.update(id, role);
		return ResponseEntity.ok().body(role);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<Role> patchUpdate(@PathVariable Long id, @RequestBody Role role){
		role = service.patchUpdate(id, role);
		return ResponseEntity.ok().body(role);
	}
}
