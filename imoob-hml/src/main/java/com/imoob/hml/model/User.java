package com.imoob.hml.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.imoob.hml.model.enums.UserStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_user")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
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
	private String cpf;

	@Getter
	@Setter
	@Column(length = 8)
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

	@JsonIgnore
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
	@JoinTable(name="tb_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles = new ArrayList<Role>();
	
	
	
	@Getter
	@Setter
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(name = "tb_user_permission", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name="permission_id"))
	private Set<Permission> permissions = new HashSet<>();
	
	

	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
//	    return List.of(new SimpleGrantedAuthority(roles.));
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
	}

	@JsonIgnore
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

//	
//	public Set<Role> getRoles() {
//		Set<Role> set = new HashSet<Role>();
//		for(UserRole x : roles) {
//			set.add(x.getRole());
//		}
//		return set;
//	}


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
