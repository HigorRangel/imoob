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

import com.imoob.hml.model.Permission;
import com.imoob.hml.service.PermissionService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/permissions")
@RequiredArgsConstructor
public class PermissionController {
	
	private final PermissionService service;
	
	@GetMapping("/")
	public ResponseEntity<List<Permission>> findAll(Pageable pageable){
		List<Permission> list = service.findAll(pageable);
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Permission> findById(@PathVariable Long id){
		Permission permission = service.findById(id);
		return ResponseEntity.ok().body(permission);
	}
	
	@PostMapping("/")
	public ResponseEntity<Permission> insert(@RequestBody Permission permission, HttpServletRequest request){
		permission = service.insert(permission, request);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(permission.getId()).toUri();
		
		return ResponseEntity.created(uri).body(permission); 
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Permission> update(@PathVariable Long id, @RequestBody Permission permission){
		permission = service.update(id, permission);
		return ResponseEntity.ok().body(permission);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<Permission> patchUpdate(@PathVariable Long id, @RequestBody Permission permission){
		permission = service.patchUpdate(id, permission);
		return ResponseEntity.ok().body(permission);
	}
}
