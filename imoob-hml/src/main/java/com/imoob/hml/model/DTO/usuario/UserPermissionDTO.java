package com.imoob.hml.model.DTO.usuario;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.imoob.hml.model.User;
import com.imoob.hml.model.DTO.permission.PermissionDTO;
import com.imoob.hml.model.enums.UserStatus;

import lombok.Getter;

@Getter
public class UserPermissionDTO {
	
	private String firstName;
	private String middleNames;
	private String lastName;
	private String email;
	private Character status;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
	private Instant lastUpdate;
	
	private List<PermissionDTO> permissions;


	public UserPermissionDTO(User user) {
		super();
		this.firstName = user.getFirstName();
		this.middleNames = user.getMiddleNames();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.status = user.getStatus().getStatusKey();
		this.lastUpdate = user.getInactived();
		this.permissions = user.getPermissions()
				.stream()
				.map(PermissionDTO::new)
				.collect(Collectors.toList());
	}

	public UserStatus getStatus() {
		return UserStatus.valueOf(status);
	}

	public void setStatus(UserStatus status) {
		this.status = status.getStatusKey();
	}
}
