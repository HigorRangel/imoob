package com.imoob.hml.model;

import java.io.Serializable;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.imoob.hml.model.pk.UserRolePk;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

//@Entity
//@Table(name = "tb_user_role")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class UserRole implements Serializable{
	

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	UserRolePk id = new UserRolePk();
	
	@NonNull
	private Instant dateInsert;
	
	
	public UserRole(User user, Role role) {
		super();
		id.setUser(user);
		id.setRole(role);
		this.dateInsert = Instant.now();
		
	}
	
	
	@JsonIgnore
	public User getUser() {
		return id.getUser();
	}
	
	public void setUser(User user) {
		this.id.setUser(user);
	}
	
	public Role getRole() {
		return id.getRole();
	}
	
	public void setRole(Role role) {
		this.id.setRole(role);
	}
}
