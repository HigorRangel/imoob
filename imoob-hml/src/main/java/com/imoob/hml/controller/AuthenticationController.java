package com.imoob.hml.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imoob.hml.model.AuthenticationRequest;
import com.imoob.hml.model.AuthenticationResponse;
import com.imoob.hml.model.RegisterRequest;
import com.imoob.hml.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationService service;

	@PostMapping("/register/")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
		
		AuthenticationResponse rr = service.register(request);
		return ResponseEntity.ok(rr);
		
//		try {
//			AuthenticationResponse rr = service.register(request);
//			return ResponseEntity.ok(rr);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
	}

	@PostMapping("/authenticate/")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request) {
		return ResponseEntity.ok(service.authenticate(request));
	}
}
