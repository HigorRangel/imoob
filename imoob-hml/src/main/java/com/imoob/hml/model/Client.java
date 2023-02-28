package com.imoob.hml.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.imoob.hml.model.User.UserBuilder;
import com.imoob.hml.model.enums.BrazillianState;
import com.imoob.hml.model.enums.Gender;
import com.imoob.hml.model.enums.MaritalStatus;
import com.imoob.hml.model.enums.UserStatus;
import com.imoob.hml.service.utils.converters.CepConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "client")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@PrimaryKeyJoinColumn(name = "user_id")
@DiscriminatorValue("client")
public class Client extends User {

	private static final long serialVersionUID = 1L;

	@Column(length = 8)
	@Convert(converter = CepConverter.class)
	private String cepAddress;

	@Column(length = 75)
	private String street;

	@Column(length = 12)
	private String numberAddress;

	@Column(length = 50)
	private String complementAddress;

	@Column(length = 50)
	private String district;

	@Column(length = 50)
	private String city;

	@Column(length = 2)
	private String state;

	@Column(length = 10)
	private String rg;

	@Column(length = 1)
	private String maritalStatus;

	@Column(length = 1)
	private String gender;
	
	@OneToOne
	@JoinColumn(name = "tb_user_type_id")
	private UserType userType;
	

	public BrazillianState getState() {
		return this.state != null ? BrazillianState.valueOfAcronym(this.state) : null;
	}

	public void setState(BrazillianState state) {
		this.state = state.getAcronym();
	}

	public MaritalStatus getMaritalStatus() {
		return this.maritalStatus != null ? MaritalStatus.fromCode(this.maritalStatus) : null;
	}

	public void setMaritalStatus(MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus.getCode();
	}

	public Gender getGender() {
		return this.gender != null ? Gender.fromValue(this.gender) : null;
	}

	public void setGender(Gender gender) {
		this.gender = gender.getName();
	}

	@Builder
	public Client(Long id, String firstName, String middleNames, String lastName, String email, Character status,
			String cpf, LocalDate birthDate, Instant created, Instant inactived, Instant lastUpdate,
			@NonNull String password, List<Role> roles, Set<UserPermission> permissions, RealEstate realEstate,
			List<UserType> userTypes, String cepAddress, String street, String numberAddress, String complementAddress,
			String district, String city, String state, String rg, String maritalStatus, String gender,
			UserType userType) {
		super(id, firstName, middleNames, lastName, email, status, cpf, birthDate, created, inactived, lastUpdate,
				password, roles, permissions, realEstate, userTypes);
		this.cepAddress = cepAddress;
		this.street = street;
		this.numberAddress = numberAddress;
		this.complementAddress = complementAddress;
		this.district = district;
		this.city = city;
		this.state = state;
		this.rg = rg;
		this.maritalStatus = maritalStatus;
		this.gender = gender;
		this.userType = userType;
	}
	
	
	public static class ClientBuilder {
		Character status;

		public ClientBuilder status(UserStatus status) {
			this.status = status.getStatusKey();
			return this;
		}

	}


	public List<UserType> getUserTypes() {
		return getUserTypes();		
	}

}
