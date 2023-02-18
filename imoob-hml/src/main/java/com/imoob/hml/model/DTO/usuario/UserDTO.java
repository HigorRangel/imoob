package com.imoob.hml.model.DTO.usuario;

import java.time.Instant;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.imoob.hml.model.User;
import com.imoob.hml.model.DTO.realEstate.RealEstateDTO;
import com.imoob.hml.model.enums.UserStatus;
import com.imoob.hml.service.utils.StringUtils;

import lombok.Getter;

@Getter
public class UserDTO {

	private Long id;
	private String fullName;
	private Character status;
	
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
	private Instant created;
	private Instant inactived;
	private Instant lastUpdate;
	
	private RealEstateDTO realEstate;

	public UserDTO(User user) {
		super();
		this.id = user.getId();
		this.fullName = user.getFirstName() + " "
				.concat(!StringUtils.isNullOrEmpty(user.getMiddleNames()) ? user.getMiddleNames()+ " " : "")
				.concat(user.getLastName());
		this.status = user.getStatus().getStatusKey();
		this.created = user.getCreated();
		this.inactived = user.getInactived();
		this.lastUpdate = user.getInactived();
		this.realEstate = new RealEstateDTO(user.getRealEstate());
	}

	public UserStatus getStatus() {
		return UserStatus.valueOf(status);
	}

	public void setStatus(UserStatus status) {
		this.status = status.getStatusKey();
	}
}
