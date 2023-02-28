package com.imoob.hml.controller;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.imoob.hml.model.Client;
import com.imoob.hml.model.User;
import com.imoob.hml.model.DTO.usuario.UserDTO;
import com.imoob.hml.model.DTO.usuario.UserPermissionAssignmentDTO;
import com.imoob.hml.model.DTO.usuario.UserPermissionDTO;
import com.imoob.hml.service.SystemActivityService;
import com.imoob.hml.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService service;
	private final SystemActivityService systemService;

	@GetMapping("/")
	@CrossOrigin(origins = "http://localhost:4200")
	// @PreAuthorize
	public ResponseEntity<List<UserDTO>> findAll(Pageable pageable, HttpServletRequest request) {

		try {
			service.findAll(pageable);
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<User> list = service.findAll(pageable);

		String remoteAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()
				.getRemoteAddr();

		System.out.println(remoteAddress);

		systemService.insertOk(request.getRequestURI(), null, request);

		List<UserDTO> listDTO = list.stream().map(user -> new UserDTO(user)).toList();

		return ResponseEntity.ok().body(listDTO);
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping(value = "/{id}")
	// @PreAuthorize
	public ResponseEntity<User> findById(@PathVariable Long id, HttpServletRequest request) {
		User obj = service.findUserById(id);

		systemService.insertOk(request.getRequestURI(), id, request);

		return ResponseEntity.ok().body(obj);
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping(value = "/realestate/{id}")
	// @PreAuthorize
	public ResponseEntity<List<UserDTO>> findAllByRealEstate(@PathVariable Long id, Pageable pageable,
			HttpServletRequest request) {
//		try {
//			service.findAllByRealEstate(pageable);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		List<User> list = service.findAllByRealEstate(id, pageable);

		String remoteAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()
				.getRemoteAddr();

		System.out.println(remoteAddress);

		List<UserDTO> listDTO = list.stream().map(user -> new UserDTO(user)).toList();

		systemService.insertOk(request.getRequestURI(), id, request);

		return ResponseEntity.ok().body(listDTO);
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/")
	public ResponseEntity<UserDTO> insert(@RequestBody Client obj, HttpServletRequest request) {
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

		systemService.insertOk(request.getRequestURI(), obj.getId(), request);

		return ResponseEntity.created(uri).body(new UserDTO(obj));
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id, HttpServletRequest request) {
		service.delete(id);

		systemService.insertOk(request.getRequestURI(), id, request);

		return ResponseEntity.noContent().build();
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@PutMapping(value = "/{id}")
	public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User obj, HttpServletRequest request) {
		obj = service.update(id, obj);

		systemService.insertOk(request.getRequestURI(), id, request);

		return ResponseEntity.ok(obj);
	}

//	@PatchMapping(value = "/{id}")
//	public ResponseEntity<User> patchUpdate(@PathVariable Long id, @RequestBody User obj){
//		obj = service.patchUpdate(id, obj);
//		return ResponseEntity.ok(obj);
//	}

	@CrossOrigin(origins = "http://localhost:4200")
	@PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
	public ResponseEntity<User> patchUpdate(@PathVariable Long id, @RequestBody JsonPatch patch, HttpServletRequest request) {
		User user = service.patchUpdate(id, patch);
		
		systemService.insertOk(request.getRequestURI(), id, request);


		return ResponseEntity.ok(user);
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/permission/")
	public ResponseEntity<UserPermissionDTO> assignPermission(@RequestBody UserPermissionAssignmentDTO obj, HttpServletRequest request) {
		User userSaved = service.assignPermission(obj);
		systemService.insertOk(request.getRequestURI(), userSaved.getId(), request);

		return ResponseEntity.ok().body(new UserPermissionDTO(userSaved));
	}
	@CrossOrigin(origins = "http://localhost:4200")
	@DeleteMapping("/permission/")
	public ResponseEntity<UserPermissionDTO> unassignPermission(@RequestBody UserPermissionAssignmentDTO obj, HttpServletRequest request) {
		User user = service.unassignPermission(obj);
		systemService.insertOk(request.getRequestURI(), user.getId(), request);

		return ResponseEntity.ok().body(new UserPermissionDTO(user));
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/approve-user/{id}/")
	public ResponseEntity<UserDTO> approveUser(@PathVariable Long id, HttpServletRequest request) {
		User user = service.approveUser(id);

		systemService.insertOk(request.getRequestURI(), id, request);

		return ResponseEntity.ok().body(new UserDTO(user));
	}
}
