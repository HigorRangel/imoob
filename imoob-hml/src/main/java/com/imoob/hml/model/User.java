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
import com.imoob.hml.service.utils.converters.CepConverter;
import com.imoob.hml.service.utils.converters.CpfConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table(name = "tb_user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
	@Column(length = 8)
	@Convert(converter = CepConverter.class)
	private String cepAddress;

	@Getter
	@Setter
	@Column(length = 12)
	private String numberAddress;

	@Getter
	@Setter
	@Column(length = 50)
	private String complementAddress;

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

	@OneToMany(mappedBy = "id.user", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	private Set<UserPermission> permissions = new HashSet<UserPermission>();
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "realEstate_id")
	@NotNull
	@Getter
	@Setter
	private RealEstate realEstate;
	

	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
//	    return List.of(new SimpleGrantedAuthority(roles.));
		return permissions.stream().map(userPermission -> new SimpleGrantedAuthority(userPermission.getPermission().getName())).toList();
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
		return UserStatus.valueOf(status);
	}

	public void setStatus(UserStatus status) {
		this.status = status.getStatusKey();
	}

	public Set<Permission> getPermissions() {
		return permissions.stream().map(userPermission -> userPermission.getId().getPermission())
				.collect(Collectors.toSet());
	}

	public void setPermissions(Set<UserPermission> permissions) {
		this.permissions = permissions;
	}
	
	@JsonIgnore
	public Set<UserPermission> getAllPermissions() {
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
		return Objects.hash(birthDate, cepAddress, complementAddress, cpf, created, email, firstName, id, inactived,
				lastName, lastUpdate, middleNames, numberAddress, password, status);
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
		return Objects.equals(birthDate, other.birthDate) && Objects.equals(cepAddress, other.cepAddress)
				&& Objects.equals(complementAddress, other.complementAddress) && Objects.equals(cpf, other.cpf)
				&& Objects.equals(created, other.created) && Objects.equals(email, other.email)
				&& Objects.equals(firstName, other.firstName) && Objects.equals(id, other.id)
				&& Objects.equals(inactived, other.inactived) && Objects.equals(lastName, other.lastName)
				&& Objects.equals(lastUpdate, other.lastUpdate) && Objects.equals(middleNames, other.middleNames)
				&& Objects.equals(numberAddress, other.numberAddress) && Objects.equals(password, other.password)
				&& Objects.equals(status, other.status);
	}

	public static class UserBuilder {
		Character status;

		public UserBuilder status(UserStatus status) {
			this.status = status.getStatusKey();
			return this;
		}
//		
//		public UserBuilder roles(Role role) {
//			if(this.roles == null) {
//				this.roles = new ArrayList<Role>();
//			}
//			
//			this.roles.add(role);
//			
//			return this;
//		}
	}

}
