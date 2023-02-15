package com.imoob.hml.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.imoob.hml.model.enums.ApiOperation;
import com.imoob.hml.service.utils.converters.BooleanConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "tb_permission")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class Permission implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Column(length = 50)
	private String name;
	
	@NotNull
	@Column(length = 50)
	private String displayName;
	
	@Column(length = 150)
	private String description;
	
	
	@Convert(converter = BooleanConverter.class)
	private Boolean enabled;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "route_id")
	private Route route;
	
	@JsonIgnore
	@OneToMany(mappedBy = "id.permission")
	private Set<UserPermission> users = new HashSet<>();
	
	@JsonIgnore
	@ManyToMany(mappedBy = "permissions", cascade = CascadeType.MERGE)
	private Set<Grouping> groupings = new HashSet<>();
	
	@JsonIgnore
	private Set<User> getUsers(){
		return users.stream().map(userPermission -> userPermission.getId().getUser()).collect(Collectors.toSet());
	}
	
	
}
