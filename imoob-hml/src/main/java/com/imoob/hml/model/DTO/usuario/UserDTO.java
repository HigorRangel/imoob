package com.imoob.hml.model.DTO.usuario;

import java.time.Instant;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.imoob.hml.model.User;
import com.imoob.hml.model.enums.UserStatus;

import lombok.Getter;

@Getter
public class UserDTO {

	private String firstName;
	private String middleNames;
	private String lastName;
	private String email;
	private Character status;
	private String cpf;
	private String cepAddress;
	private String numberAddress;
	private String complementAddress;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate birthDate;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")

	private Instant created;
	private Instant inactived;
	private Instant lastUpdate;

	public UserDTO(User user) {
		super();
		this.firstName = user.getFirstName();
		this.middleNames = user.getMiddleNames();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.status = user.getStatus().getStatusKey();
		this.cpf = user.getCpf();
		this.cepAddress = user.getCepAddress();
		this.numberAddress = user.getNumberAddress();
		this.complementAddress = user.getComplementAddress();
		this.birthDate = user.getBirthDate();
		this.created = user.getCreated();
		this.inactived = user.getInactived();
		this.lastUpdate = user.getInactived();
	}

	public UserStatus getStatus() {
		return UserStatus.valueOf(status);
	}

	public void setStatus(UserStatus status) {
		this.status = status.getStatusKey();
	}
}
