package com.imoob.hml.service;

import java.time.Instant;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.imoob.hml.config.JwtService;
import com.imoob.hml.model.AuthenticationRequest;
import com.imoob.hml.model.AuthenticationResponse;
import com.imoob.hml.model.RealEstate;
import com.imoob.hml.model.RegisterRequest;
import com.imoob.hml.model.User;
import com.imoob.hml.model.enums.UserStatus;
import com.imoob.hml.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service	
@RequiredArgsConstructor
public class AuthenticationService {
	
	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	
	private final UserService userService;
	
	private final RealEstateService realEstateService;
	
	public AuthenticationResponse register(RegisterRequest request) {
		
		RealEstate realEstate = realEstateService.findById(request.getRealEstate());

		var user = User.builder()
				.firstName(request.getFirstName())
				.middleNames(request.getMiddleNames())
				.lastName(request.getLastName())
				.email(request.getEmail())
				.status(UserStatus.ACTIVE)
				.cpf(request.getCpf()) 
				.password(passwordEncoder.encode(request.getPassword()))
				.birthDate(request.getBirthDate())
				.cepAddress(request.getCepAddress())
				.lastUpdate(Instant.now())
				.created(Instant.now())
				.numberAddress(request.getNumberAddress())
				.complementAddress(request.getComplementAddress())
				.realEstate(realEstate)
				.build();
		
		user = userService.insert(user);
//		repository.save(user);
		
		var jwtToken = jwtService.generateToken(user);
				
		return new AuthenticationResponse(jwtToken);
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		
		var user = repository.findByEmail(request.getEmail())
				.orElseThrow();
		
		var jwtToken = jwtService.generateToken(user);
		
		return new AuthenticationResponse(jwtToken);
	}
}
