package com.imoob.hml.model;

import java.io.Serializable;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.imoob.hml.model.pk.UserPermissionPk;
import com.imoob.hml.service.utils.converters.BooleanConverter;

import jakarta.persistence.Convert;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_user_permission")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class UserPermission implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UserPermissionPk id = new UserPermissionPk();

	@Convert(converter = BooleanConverter.class)
	private Boolean post;

	@Convert(converter = BooleanConverter.class)
	private Boolean put;

	@Convert(converter = BooleanConverter.class)
	private Boolean delete;

	@Convert(converter = BooleanConverter.class)
	private Boolean patch;

	@Convert(converter = BooleanConverter.class)
	private Boolean get;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
	private Instant assignmentDate;

	public UserPermission(User user, Permission permission, Boolean post, Boolean put, Boolean delete, Boolean patch,
			Boolean get, Instant assignmentDate) {
		this.id.setUser(user);
		this.id.setPermission(permission);
		this.post = post;
		this.put = put;
		this.delete = delete;
		this.patch = patch;
		this.get = get;
		this.assignmentDate = assignmentDate;
	}

	public Permission getPermission() {
		return this.id.getPermission();
	}

	public void setPermission(Permission permission) {
		this.id.setPermission(permission);
	}

	public User getUser() {
		return this.id.getUser();
	}

	public void setUser(User user) {
		this.id.setUser(user);
	}
}
