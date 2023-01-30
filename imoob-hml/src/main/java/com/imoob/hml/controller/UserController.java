package com.imoob.hml.controller;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.github.fge.jsonpatch.JsonPatch;
import com.imoob.hml.model.User;
import com.imoob.hml.model.DTO.usuario.UserDTO;
import com.imoob.hml.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/users")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService service;
	
	@GetMapping("/")
	//@PreAuthorize
	public ResponseEntity<List<UserDTO>> findAll(Pageable pageable){
		
		try {
			service.findAll(pageable);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		List<User> list = service.findAll(pageable);
		
		String remoteAddress = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes())
				.getRequest().getRemoteAddr();
		
		System.out.println(remoteAddress);
		
		List<UserDTO> listDTO = list.stream().map(user -> new UserDTO(user)).toList();
		
		return ResponseEntity.ok().body(listDTO);
	}
	
	@GetMapping(value = "/{id}")
	//@PreAuthorize
	public ResponseEntity<User> findById(@PathVariable Long id){
		User obj = service.findUserById(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@PostMapping("/")
	public ResponseEntity<User> insert(@RequestBody User obj){
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(obj);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User obj){
		obj = service.update(id, obj);
		return ResponseEntity.ok(obj);
	}
	
//	@PatchMapping(value = "/{id}")
//	public ResponseEntity<User> patchUpdate(@PathVariable Long id, @RequestBody User obj){
//		obj = service.patchUpdate(id, obj);
//		return ResponseEntity.ok(obj);
//	}
	
	@PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
	public ResponseEntity<User> patchUpdate(@PathVariable Long id, @RequestBody JsonPatch patch){
			User user = service.patchUpdate(id, patch);
			return ResponseEntity.ok(user);
	}
}
