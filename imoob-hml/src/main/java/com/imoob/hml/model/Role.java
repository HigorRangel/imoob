package com.imoob.hml.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Builder
@ToString
@Table(name = "tb_role")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NonNull
	@Column(length = 50)
	private String name;

	@NonNull
	@Column(length = 50)
	private String displayName;

	@NonNull
	@Column(length = 150)
	private String description;

	@JsonIgnore
	@Getter
	@Setter
	@ManyToMany(mappedBy = "roles", cascade = CascadeType.MERGE)
	private List<User> users = new ArrayList<>();


	@ManyToOne
	@JoinColumn(name = "realEstate_id")
	private RealEstate realEstate;
	
	//	@JsonIgnore
//	public Set<User> getUsers(){
//		Set<User> set = new HashSet<User>();
//		for(UserRole  x: users) {
//			set.add(x.getUser());
//		}
//		return set;
//	}

}
