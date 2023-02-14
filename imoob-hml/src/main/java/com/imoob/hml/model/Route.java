package com.imoob.hml.model;

import java.io.Serializable;

import com.imoob.hml.model.enums.ApiOperation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_route")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Builder
public class Route implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(length = 100)
	private String route;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "systemClass_id")
	private SystemClass systemClass;
	
	@NotNull
	private String operation;

	
	public ApiOperation getOperation() {
		return ApiOperation.getByName(operation);
	}
	
	public void setOperation(ApiOperation operation) {
		this.operation = operation.getName();
	}

	public Route(Long id, String route, SystemClass systemClass,  ApiOperation operation) {
		super();
		this.id = id;
		this.route = route;
		this.systemClass = systemClass;
		this.operation = operation.getName();
	}

	
}
