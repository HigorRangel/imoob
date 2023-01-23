package com.imoob.hml.config;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.imoob.hml.model.Role;
import com.imoob.hml.model.User;
import com.imoob.hml.model.UserRole;
import com.imoob.hml.model.enums.UserStatus;
import com.imoob.hml.repository.RoleRepository;
import com.imoob.hml.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@Profile("test")
@RequiredArgsConstructor
public class TesteConfig implements CommandLineRunner{
	
	private final UserRepository userRepository;
	
	private final RoleRepository roleRepository;
	
	private final PasswordEncoder passwordEncoder;

//	private final UserRoleRepository userRoleRepository;
		
	@Override
	public void run(String... args) throws Exception {
		User user1 = User.builder()
				.birthDate(Instant.parse("2000-07-06T00:00:00Z"))
				.cepAddress("13473758")
				.cpf("48274702867")
				.created(Instant.now())
				.email("higorrg2000@hotmail.com")
				.firstName("Higor")
				.lastName("Rangel")
				.lastUpdate(Instant.now())
				.numberAddress("1008")
				.password(passwordEncoder.encode("45945261"))
				.status(UserStatus.ACTIVE)
				.roles(new ArrayList<>())
				.build();
		
		

		
		Role role1 = Role.builder()
				.description("Acesso de Administrador")
				.displayName("Acesso Administrador")
				.name("ACESSO_ADMINISTRADOR").build();
		
		Role role2 = Role.builder()
				.description("Acesso Mínimo")
				.displayName("Acesso Mínimo")
				.name("ACESSO_MINIMO").build();
		
		role1 = roleRepository.save(role1);
		role2 = roleRepository.save(role2);
		
		
		user1.getRoles().add(role1);
		user1.getRoles().add(role2);
		
		userRepository.save(user1);

		
//		UserRole ur1 = new UserRole(user1, role1);
//		UserRole ur2 = new UserRole(user1, role2);
//			
//		userRoleRepository.save(ur1);
//		userRoleRepository.save(ur2);
		

		
	}

}
