package com.imoob.hml.model;

import java.io.Serializable;
import java.time.Instant;

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
public class UserPermission  implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UserPermissionPk id = new UserPermissionPk();
	
	@Convert(converter = BooleanConverter.class)
	private Boolean create;
	
	@Convert(converter = BooleanConverter.class)
	private Boolean edit;
	
	@Convert(converter = BooleanConverter.class)
	private Boolean delete;
	
	
	private Instant assignmentDate;
}
