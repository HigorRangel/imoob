package com.imoob.hml.config;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.imoob.hml.model.AuthenticationRequest;
import com.imoob.hml.model.AuthenticationResponse;
import com.imoob.hml.model.ClassField;
import com.imoob.hml.model.Grouping;
import com.imoob.hml.model.Permission;
import com.imoob.hml.model.RealEstate;
import com.imoob.hml.model.Role;
import com.imoob.hml.model.SystemClass;
import com.imoob.hml.model.User;
import com.imoob.hml.model.UserPermission;
import com.imoob.hml.model.enums.UserStatus;
import com.imoob.hml.repository.ClassFieldRepository;
import com.imoob.hml.repository.PermissionRepository;
import com.imoob.hml.repository.RealEstateRepository;
import com.imoob.hml.repository.RoleRepository;
import com.imoob.hml.repository.SystemClassRepository;
import com.imoob.hml.repository.UserPermissionRepository;
import com.imoob.hml.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@Profile("test")
@RequiredArgsConstructor
public class TesteConfig implements CommandLineRunner {

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	private final PermissionRepository permissionRepository;

	private final PasswordEncoder passwordEncoder;

	private final UserPermissionRepository userPermissionRepository;

	private final RealEstateRepository realEstateRepository;

	private final SystemClassRepository systemClassRepository;

	private final ClassFieldRepository classFieldRepository;
//	private final UserRoleRepository userRoleRepository;

	@Override
	public void run(String... args) throws Exception {
		User user1 = User.builder().birthDate(LocalDate.parse("10/09/2000", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
				.cepAddress("13473758").cpf("07553675105").created(Instant.now()).email("rood_alves@hotmail.com")
				.firstName("Rodrigo").middleNames("Alves").lastName("Ribeiro").lastUpdate(Instant.now())
				.numberAddress("1008").password(passwordEncoder.encode("45945261")).status(UserStatus.ACTIVE)
				.roles(new ArrayList<>()).permissions(new HashSet<UserPermission>()).build();

		Role role1 = Role.builder().description("Acesso de Administrador").displayName("Acesso Administrador")
				.name("ACESSO_ADMINISTRADOR").build();

		Role role2 = Role.builder().description("Acesso Mínimo").displayName("Acesso Mínimo").name("ACESSO_MINIMO")
				.build();

		Permission permission1 = Permission.builder().description("Cadastro de Usuário")
				.displayName("Cadastro de Usuário").name("CADASTRO_USUARIO").path("/api/users/").operation("POST")
				.enabled(true).build();

		role1 = roleRepository.save(role1);
		role2 = roleRepository.save(role2);

		permission1 = permissionRepository.save(permission1);

		RealEstate realEstate1 = RealEstate.builder().cnpj("36.422.027/0001-10").corporateName("Imobiliária Teste LTDA")
				.tradingName("Imobiliária Teste").created(Instant.now()).website("www.imobiliaria.com.br")
				.businessEmail("teste@imobiliariateste.com.br").status('A').build();

		realEstate1 = realEstateRepository.save(realEstate1);

		user1.getRoles().add(role1);
		user1.getRoles().add(role2);
		user1.setRealEstate(realEstate1);

		userRepository.save(user1);

		UserPermission up1 = new UserPermission(user1, permission1, true, true, true, true, true, Instant.now());
		userPermissionRepository.save(up1);

		// Registro de todas as classes do sistema
		// *** User ***
		SystemClass classUser = setClass(User.class, "Usuário");
		classUser = systemClassRepository.save(classUser);
		setClassFields(classUser);

		// *** AuthenticationRequest ***
		SystemClass classAuthenticationRequest = setClass(AuthenticationRequest.class, "Requisição de Autenticação");
		classAuthenticationRequest = systemClassRepository.save(classAuthenticationRequest);
		setClassFields(classAuthenticationRequest);

		// *** AuthenticationResponse ***
		SystemClass classAuthenticationResponse = setClass(AuthenticationResponse.class, "Requisição de Resposta");
		classAuthenticationResponse = systemClassRepository.save(classAuthenticationResponse);
		setClassFields(classAuthenticationResponse);
		
		// *** ClassField ***
		SystemClass clasClassField = setClass(ClassField.class, "Campos da Classe");
		clasClassField = systemClassRepository.save(clasClassField);
		setClassFields(clasClassField);
		
		// *** Grouping ***
		SystemClass classGrouping = setClass(Grouping.class, "Agrupamento");
		classGrouping = systemClassRepository.save(classGrouping);
		setClassFields(classGrouping);

	}

	private SystemClass setClass(Class<?> clazz, String classNameTranslate) {
		return SystemClass.builder().classSimpleName(clazz.getSimpleName()).classNameTranslate(classNameTranslate)
				.classFullName(clazz.getName()).fields(new HashSet<>()).build();
	}

	private List<Field> setClassFields(SystemClass systemClass) {

		Field[] fields = systemClass.getSystemClass().getDeclaredFields();
		for (Field field : fields) {
			if (!field.getName().equals("serialVersionUID")) {
//	        		System.out.println("Name: " + field.getName());
//		            System.out.println("Type: " + field.getType().getName());
				ClassField classField = classFieldRepository
						.save(new ClassField(null, field.getName(), "", field.getType().getName(), systemClass));
				systemClass.getFields().add(classField);
			}

		}

		return Arrays.asList(fields);
	}

}
