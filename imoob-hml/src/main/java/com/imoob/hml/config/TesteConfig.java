package com.imoob.hml.config;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.imoob.hml.model.AuthenticationResponse;
import com.imoob.hml.model.ClassField;
import com.imoob.hml.model.Client;
import com.imoob.hml.model.Grouping;
import com.imoob.hml.model.Permission;
import com.imoob.hml.model.RealEstate;
import com.imoob.hml.model.Role;
import com.imoob.hml.model.Route;
import com.imoob.hml.model.SystemActivity;
import com.imoob.hml.model.SystemClass;
import com.imoob.hml.model.User;
import com.imoob.hml.model.UserPermission;
import com.imoob.hml.model.UserRole;
import com.imoob.hml.model.UserType;
import com.imoob.hml.model.enums.ApiOperation;
import com.imoob.hml.model.enums.TypeUser;
import com.imoob.hml.model.enums.UserStatus;
import com.imoob.hml.repository.ClassFieldRepository;
import com.imoob.hml.repository.PermissionRepository;
import com.imoob.hml.repository.RealEstateRepository;
import com.imoob.hml.repository.RoleRepository;
import com.imoob.hml.repository.RouteRepository;
import com.imoob.hml.repository.SystemClassRepository;
import com.imoob.hml.repository.UserPermissionRepository;
import com.imoob.hml.repository.UserRepository;
import com.imoob.hml.repository.UserTypeRepository;

import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;

@Configuration
@Profile("test")
@RequiredArgsConstructor
public class TesteConfig implements CommandLineRunner {

	private final UserRepository userRepository;
	
	private final UserTypeRepository userTypeRepository;

	private final RoleRepository roleRepository;

	private final PermissionRepository permissionRepository;

	private final PasswordEncoder passwordEncoder;

	private final UserPermissionRepository userPermissionRepository;

	private final RealEstateRepository realEstateRepository;

	private final SystemClassRepository systemClassRepository;

	private final ClassFieldRepository classFieldRepository;
	
	private final RouteRepository routeRepository;
//	private final UserRoleRepository userRoleRepository;

	@Override
	public void run(String... args) throws Exception {
		Client client1 = Client.builder().birthDate(LocalDate.parse("10/09/2000", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
				.cepAddress("13473758").cpf("07553675105").created(Instant.now()).email("rood_alves@hotmail.com")
				.firstName("Rodrigo").middleNames("Alves").lastName("Ribeiro").lastUpdate(Instant.now())
				.numberAddress("1008").password(passwordEncoder.encode("45945261")).status(UserStatus.ACTIVE)
				.roles(new ArrayList<>()).permissions(new HashSet<UserPermission>()).userTypes(new ArrayList<>()).build();

		Role role1 = Role.builder().description("Acesso de Administrador").displayName("Acesso Administrador")
				.name("ACESSO_ADMINISTRADOR").build();

		Role role2 = Role.builder().description("Acesso Mínimo").displayName("Acesso Mínimo").name("ACESSO_MINIMO")
				.build();
		
		

		

		role1 = roleRepository.save(role1);
		role2 = roleRepository.save(role2);


		RealEstate realEstate1 = RealEstate.builder().cnpj("36.422.027/0001-10").corporateName("Imobiliária Teste LTDA")
				.tradingName("Imobiliária Teste").created(Instant.now()).website("www.imobiliaria.com.br")
				.businessEmail("teste@imobiliariateste.com.br").status('A').build();

		realEstate1 = realEstateRepository.save(realEstate1);

		client1.getRoles().add(role1);
		client1.getRoles().add(role2);
		client1.setRealEstate(realEstate1);
		UserType userType1 = new UserType(client1, TypeUser.FUNCIONARIO);
		

		
		registerClassesAndRoutes();


		
		Permission permission1 = Permission.builder().description("Cadastro de Permissões")
				.displayName("Cadastro de Permissões").name("CADASTRO_PERMISSAO").route(routeRepository.findByRouteOperation("/api/permissions/", "POST"))
				.enabled(true).users(new HashSet<UserPermission>()).build();
		
		UserPermission up1 = new UserPermission(client1, permission1, Instant.now());
		
		client1.getPermissions().add(up1);
		permission1.getUsers().add(up1);
		
		permission1 = permissionRepository.save(permission1);
		client1 = userRepository.save(client1);
		userType1 = userTypeRepository.save(userType1);
		
		userPermissionRepository.save(up1);
		
		

		
		
		

		
		getClasses();


		
	}

private void getClasses() {
	String packageName = "com.imoob.hml.model";
	Reflections reflections = new Reflections(packageName);
	Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Entity.class);
	
	classes.forEach(x ->{
		System.out.println(x.getName());
	});
}

private void registerClassesAndRoutes() {
	// Registro de todas as classes do sistema
	// *** User ***
	SystemClass classUser = setClass(User.class, "Usuário");
	classUser = systemClassRepository.save(classUser);
	setClassFields(classUser);
	setRoutes("/api/users/", classUser, ApiOperation.GET, ApiOperation.DELETE, ApiOperation.PATCH, ApiOperation.POST, ApiOperation.PUT);
	setRoutes("/api/users/:id", classUser, ApiOperation.GET);

	// *** AuthenticationResponse ***
	SystemClass classAuthenticationResponse = setClass(AuthenticationResponse.class, "Requisição de Resposta");
	classAuthenticationResponse = systemClassRepository.save(classAuthenticationResponse);
	setClassFields(classAuthenticationResponse);
	setRoutes("/api/auth/register/", classAuthenticationResponse, ApiOperation.POST);
	setRoutes("/api/auth/authenticate/", classAuthenticationResponse, ApiOperation.POST);

	// *** ClassField ***
	SystemClass clasClassField = setClass(ClassField.class, "Campos da Classe");
	clasClassField = systemClassRepository.save(clasClassField);
	setClassFields(clasClassField);
//		Route routeClassField = new Route(null, "/api/class/field/", clasClassField);
//		routeClassField = routeRepository.save(routeClassField);

	// *** Grouping ***
	SystemClass classGrouping = setClass(Grouping.class, "Agrupamento");
	classGrouping = systemClassRepository.save(classGrouping);
	setClassFields(classGrouping);
	setRoutes("/api/groupings/", classGrouping, ApiOperation.POST, ApiOperation.PATCH);


	// *** Permission ***
	SystemClass classPermission = setClass(Permission.class, "Permissões");
	classPermission = systemClassRepository.save(classPermission);
	setClassFields(classPermission);
	setRoutes("/api/permissions/", classPermission, ApiOperation.GET, ApiOperation.DELETE, ApiOperation.PATCH, ApiOperation.POST, ApiOperation.PUT);
	setRoutes("/api/permissions/:id", classPermission, ApiOperation.GET);

	// *** RealEstate ***
	SystemClass classRealEstate = setClass(RealEstate.class, "Imobiliária");
	classRealEstate = systemClassRepository.save(classRealEstate);
	setClassFields(classRealEstate);
	setRoutes("/api/realestate/", classRealEstate, ApiOperation.GET, ApiOperation.DELETE, ApiOperation.PATCH, ApiOperation.POST, ApiOperation.PUT);
	setRoutes("/api/realestate/:id", classRealEstate, ApiOperation.GET);

//		// *** RegisterRequest ***
//		SystemClass classRegisterRequest = setClass(RegisterRequest.class, "Requisição de Registro");
//		classRegisterRequest = systemClassRepository.save(classRegisterRequest);
//		setClassFields(classRegisterRequest);
//		Route routeRegisterRequest = new Route(null, "/api/register-requests/", classRegisterRequest);
//		routeRegisterRequest = routeRepository.save(routeRegisterRequest);

	// *** Role ***
	SystemClass classRole = setClass(Role.class, "Cargo");
	classRole = systemClassRepository.save(classRole);
	setClassFields(classRole);
	setRoutes("/api/roles/", classRole, ApiOperation.GET, ApiOperation.DELETE, ApiOperation.PATCH, ApiOperation.POST, ApiOperation.PUT);
	setRoutes("/api/roles/:id", classRole, ApiOperation.GET);

	// *** Route ***
	SystemClass classRoute = setClass(Route.class, "Rota");
	classRoute = systemClassRepository.save(classRoute);
	setClassFields(classRoute);
//		setRoutes("/api/route/", classUser, ApiOperation.GET, ApiOperation.DELETE, ApiOperation.PATCH, ApiOperation.POST, ApiOperation.PUT);
//		setRoutes("/api/route/:id", classUser, ApiOperation.GET);

	// *** SystemActivity ***
	SystemClass classSystemActivity = setClass(SystemActivity.class, "Atividade do Sistema");
	classSystemActivity = systemClassRepository.save(classSystemActivity);
	setClassFields(classSystemActivity);
//		setRoutes("/api/systemactivity/", classUser, ApiOperation.GET, ApiOperation.DELETE, ApiOperation.PATCH, ApiOperation.POST, ApiOperation.PUT);
//		setRoutes("/api/systemactivity/:id", classUser, ApiOperation.GET);

	// *** SystemClass ***
	SystemClass classSystemClass = setClass(SystemClass.class, "Classe do Sistema");
	classSystemClass = systemClassRepository.save(classSystemClass);
	setClassFields(classSystemClass);
//		setRoutes("/api/systemclass/", classUser, ApiOperation.GET, ApiOperation.DELETE, ApiOperation.PATCH, ApiOperation.POST, ApiOperation.PUT);
//		setRoutes("/api/systemclass/:id", classUser, ApiOperation.GET);

	// *** UserPermission ***
	SystemClass classUserPermission = setClass(UserPermission.class, "Permissão de Usuário");
	classUserPermission = systemClassRepository.save(classUserPermission);
	setClassFields(classUserPermission);
//		setRoutes("/api/userpermissions/", classUser, ApiOperation.GET, ApiOperation.DELETE, ApiOperation.PATCH, ApiOperation.POST, ApiOperation.PUT);
//		setRoutes("/api/userpermissions/:id", classUser, ApiOperation.GET);

	// *** UserRole ***
	SystemClass classUserRole = setClass(UserRole.class, "Cargo de Usuário");
	classUserRole = systemClassRepository.save(classUserRole);
	setClassFields(classUserRole);
//		setRoutes("/api/userroles/", classUser, ApiOperation.GET, ApiOperation.DELETE, ApiOperation.PATCH, ApiOperation.POST, ApiOperation.PUT);
//		setRoutes("/api/userroles/:id", classUser, ApiOperation.GET);
}

	private void setRoutes(String path,SystemClass systemClass, ApiOperation ... operations) {
		for(ApiOperation operation : operations) {
			Route routeUserGET = new Route(null, path, systemClass, operation);
			routeUserGET = routeRepository.save(routeUserGET);
		}
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
