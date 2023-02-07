package com.imoob.hml.config;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.imoob.hml.model.Permission;
import com.imoob.hml.model.RealEstate;
import com.imoob.hml.model.Role;
import com.imoob.hml.model.User;
import com.imoob.hml.model.UserPermission;
import com.imoob.hml.model.enums.UserStatus;
import com.imoob.hml.repository.PermissionRepository;
import com.imoob.hml.repository.RealEstateRepository;
import com.imoob.hml.repository.RoleRepository;
import com.imoob.hml.repository.UserPermissionRepository;
import com.imoob.hml.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@Profile("test")
@RequiredArgsConstructor
public class TesteConfig implements CommandLineRunner{
	
	private final UserRepository userRepository;
	
	private final RoleRepository roleRepository;
	
	private final PermissionRepository permissionRepository;
	
	private final PasswordEncoder passwordEncoder;

	private final UserPermissionRepository userPermissionRepository;
	
	private final RealEstateRepository realEstateRepository;
//	private final UserRoleRepository userRoleRepository;
		
	@Override
	public void run(String... args) throws Exception {
		User user1 = User.builder()
				.birthDate(LocalDate.parse("10/09/2000", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
				.cepAddress("13473758")
				.cpf("07553675105")
				.created(Instant.now())
				.email("rood_alves@hotmail.com")
				.firstName("Rodrigo")
				.middleNames("Alves")
				.lastName("Ribeiro")
				.lastUpdate(Instant.now())
				.numberAddress("1008")
				.password(passwordEncoder.encode("45945261"))
				.status(UserStatus.ACTIVE)
				.roles(new ArrayList<>())
				.permissions(new HashSet<UserPermission>())
				.build();
		
		

		
		Role role1 = Role.builder()
				.description("Acesso de Administrador")
				.displayName("Acesso Administrador")
				.name("ACESSO_ADMINISTRADOR").build();
		
		Role role2 = Role.builder()
				.description("Acesso Mínimo")
				.displayName("Acesso Mínimo")
				.name("ACESSO_MINIMO").build();
		
		
		Permission permission1 = Permission.builder()
				.description("Cadastro de Usuário")
				.displayName("Cadastro de Usuário")
				.name("CADASTRO_USUARIO")
				.build();
		
		role1 = roleRepository.save(role1);
		role2 = roleRepository.save(role2);
		
		permission1 = permissionRepository.save(permission1);
		
		RealEstate realEstate1 = RealEstate.builder()
				.cnpj("36.422.027/0001-10")
				.corporateName("Imobiliária Teste LTDA")
				.tradingName("Imobiliária Teste")
				.created(Instant.now())
				.website("www.imobiliaria.com.br")
				.businessEmail("teste@imobiliariateste.com.br")
				.status('A')
				.build();
		
		
		realEstate1 = realEstateRepository.save(realEstate1);
		
		user1.getRoles().add(role1);
		user1.getRoles().add(role2);
		user1.setRealEstate(realEstate1);
		
		userRepository.save(user1);

		
		UserPermission up1 = new UserPermission(user1, permission1, true, true, true, true, true, Instant.now());
		userPermissionRepository.save(up1);
		
		
//		RealEstate re1 = new RealEstate("20.293.671/0001-03", "Teste1", "Teste2", "A", Instant.now(), "higor@rangel.com", "teste");
		
//		realEstateRepository.save(re1);
		
		
		

		

		
//		UserRole ur1 = new UserRole(user1, role1);
//		UserRole ur2 = new UserRole(user1, role2);
//			
//		userRoleRepository.save(ur1);
//		userRoleRepository.save(ur2);
		

		
	}

}
