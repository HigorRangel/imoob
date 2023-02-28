package com.imoob.hml.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.imoob.hml.model.enums.UserStatus;
import com.imoob.hml.service.utils.converters.CpfConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode.Exclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tb_user")
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements Serializable, UserDetails {

	private static final long serialVersionUID = 1L;
	@Id
	@Getter
	@Setter
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Getter
	@Setter
	@Column(length = 25)
	private String firstName;

	@Column(length = 25)
	@Getter
	@Setter
	private String middleNames;

	@NotNull
	@Getter
	@Setter
	@Column(length = 25)
	private String lastName;

	@NotNull
	@Getter
	@Setter
	@Column(length = 50)
	private String email;

//	@Getter @Setter
//	@Enumerated(EnumType.STRING)
//	@NonNull
//	private UserStatus status;

	@NotNull
	private Character status;

//	private Character status;

	@NotNull
	@Getter
	@Setter
	@Column(length = 11)
	@CPF
	@Convert(converter = CpfConverter.class)
	private String cpf;

	@Getter
	@Setter
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate birthDate;

	@Getter
	@Setter
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
	private Instant created;

	@Getter
	@Setter
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
	private Instant inactived;

	@Getter
	@Setter
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
	private Instant lastUpdate;

	@NonNull
	@Setter
	private String password;

//	@Setter
//	@Getter
//	@OneToMany(mappedBy = "id.user")
//	private Set<UserRole> roles = new HashSet<>();

	@Getter
	@Setter
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(name = "tb_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles = new ArrayList<Role>();

	@Exclude
	@OneToMany(mappedBy = "id.user", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	private Set<UserPermission> permissions = new HashSet<UserPermission>();
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "realEstate_id")
	@NotNull
	@Getter
	@Setter
	private RealEstate realEstate;

	@OneToMany(mappedBy = "user")
	private List<UserType> userTypes;

	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
//	    return List.of(new SimpleGrantedAuthority(roles.));
		return permissions.stream()
				.map(userPermission -> new SimpleGrantedAuthority(userPermission.getPermission().getName())).toList();
	}

	@Override
	public String getPassword() {
		return password;
	}

	@JsonIgnore
	@Override
	public String getUsername() {
		return email;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return !(UserStatus.valueOf(status) == UserStatus.EXPIRED);
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return !(UserStatus.valueOf(status) == UserStatus.BLOCKED);
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return (UserStatus.valueOf(status) == UserStatus.ACTIVE);
	}

	public UserStatus getStatus() {
		return (this.status != null) ? UserStatus.valueOf(this.status) : null;
	}

	public void setStatus(UserStatus status) {
		this.status = status.getStatusKey();
	}

	public Set<Permission> getAllPermissions() {
		return permissions.stream().map(userPermission -> userPermission.getId().getPermission())
				.collect(Collectors.toSet());
	}

	public void setPermissions(Set<UserPermission> permissions) {
		this.permissions = permissions;
	}

	@JsonIgnore
	public Set<UserPermission> getPermissions() {
		return this.permissions;
	}

//	
//	public Set<Role> getRoles() {
//		Set<Role> set = new HashSet<Role>();
//		for(UserRole x : roles) {
//			set.add(x.getRole());
//		}
//		return set;
//	}
	
	
	

	@Override
	public int hashCode() {
		return Objects.hash(birthDate, cpf, created, email, firstName, id, inactived, lastName, lastUpdate, middleNames,
				password, permissions, realEstate, roles, status);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(birthDate, other.birthDate) && Objects.equals(cpf, other.cpf)
				&& Objects.equals(created, other.created) && Objects.equals(email, other.email)
				&& Objects.equals(firstName, other.firstName) && Objects.equals(id, other.id)
				&& Objects.equals(inactived, other.inactived) && Objects.equals(lastName, other.lastName)
				&& Objects.equals(lastUpdate, other.lastUpdate) && Objects.equals(middleNames, other.middleNames)
				&& Objects.equals(password, other.password) && Objects.equals(permissions, other.permissions)
				&& Objects.equals(realEstate, other.realEstate) && Objects.equals(roles, other.roles)
				&& Objects.equals(status, other.status);
	}

	public static class UserBuilder {
		Character status;

		public UserBuilder status(UserStatus status) {
			this.status = status.getStatusKey();
			return this;
		}

	}

//	public static UserBuilder builder() {
//		return new UserBuilder();
//	}
//
//	public static class UserBuilder {
//
//	    private Long id;
//	    private String firstName;
//	    private String middleNames;
//	    private String lastName;
//	    private String email;
//	    private Character status;
//	    private String cpf;
//	    private LocalDate birthDate;
//	    private String password;
//	    private List<Role> roles = new ArrayList<Role>();
//	    private Set<UserPermission> permissions = new HashSet<UserPermission>();
//	    private RealEstate realEstate;
//	    private List<UserType> userTypes;
//	    private Instant created;
//	    private Instant inactived;
//	    private Instant lastUpdate;
//
//	    public UserBuilder id(Long id) {
//	        this.id = id;
//	        return this;
//	    }
//
//	    public UserBuilder firstName(String firstName) {
//	        this.firstName = firstName;
//	        return this;
//	    }
//
//	    public UserBuilder middleNames(String middleNames) {
//	        this.middleNames = middleNames;
//	        return this;
//	    }
//
//	    public UserBuilder lastName(String lastName) {
//	        this.lastName = lastName;
//	        return this;
//	    }
//
//	    public UserBuilder email(String email) {
//	        this.email = email;
//	        return this;
//	    }
//
//	    public UserBuilder status(Character status) {
//	        this.status = status;
//	        return this;
//	    }
//
//	    public UserBuilder cpf(String cpf) {
//	        this.cpf = cpf;
//	        return this;
//	    }
//
//	    public UserBuilder birthDate(LocalDate birthDate) {
//	        this.birthDate = birthDate;
//	        return this;
//	    }
//
//	    public UserBuilder password(String password) {
//	        this.password = password;
//	        return this;
//	    }
//
//	    public UserBuilder roles(List<Role> roles) {
//	        this.roles = roles;
//	        return this;
//	    }
//
//	    public UserBuilder permissions(Set<UserPermission> permissions) {
//	        this.permissions = permissions;
//	        return this;
//	    }
//
//	    public UserBuilder realEstate(RealEstate realEstate) {
//	        this.realEstate = realEstate;
//	        return this;
//	    }
//
//	    public UserBuilder userTypes(List<UserType> userTypes) {
//	        this.userTypes = userTypes;
//	        return this;
//	    }
//
//	    public UserBuilder created(Instant created) {
//	        this.created = created;
//	        return this;
//	    }
//
//	    public UserBuilder inactived(Instant inactived) {
//	        this.inactived = inactived;
//	        return this;
//	    }
//
//	    public UserBuilder lastUpdate(Instant lastUpdate) {
//	        this.lastUpdate = lastUpdate;
//	        return this;
//	    }
//
//	    public User build() {
//	        return new User(id, firstName, middleNames, lastName, email, status, cpf, birthDate, created, inactived, lastUpdate, password, roles, permissions, realEstate, userTypes);
//	    }
//	}


}
