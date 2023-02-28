package com.imoob.hml.model;

import java.io.Serializable;

import com.imoob.hml.model.enums.TypeUser;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tb_user_type")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class UserType implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "user_id", unique = true, nullable = false)
	private User user;
	
	
	private Character userType;
	
	

	
	public TypeUser getUserType() {
		return TypeUser.valueOf(userType);
	}
	
	public void setUserType(TypeUser typeUser) {
		this.userType = typeUser.getAcronym();
	}

	public UserType(User user, TypeUser userType) {
		super();
		this.user = user;
		this.userType = userType.getAcronym();
	}
}
