package com.imoob.hml.model;

import java.io.Serializable;
import java.time.Instant;

import org.hibernate.validator.constraints.br.CNPJ;

import com.imoob.hml.model.enums.RealStateStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_real_state")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Getter
@Setter
public class RealEstate implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@CNPJ
	@NotNull
	@Column(length = 14)
	private String cnpj;
	
	@Column(length = 100)
	private String corporateName;
	
	@Column(length = 100)
	private String tradingName;
	
	private String status;
	
	private Instant created;
	
	@Email
	@Column(length = 100)
	private String businessEmail;
	
	@Column(length = 100)
	private String website;
	
	
	public RealEstate(@CNPJ @NotNull String cnpj, String corporateName, String tradingName, String status,
			Instant created, @Email String businessEmail, String website) {
		super();
		this.cnpj = cnpj;
		this.corporateName = corporateName;
		this.tradingName = tradingName;
		this.status = status;
		this.created = created;
		this.businessEmail = businessEmail;
		this.website = website;
	}

	
	public RealStateStatus getStatus() {
		return RealStateStatus.valueOf(status);
	}
	
	public void setStatus(RealStateStatus status) {
		this.status = status.getValue();
	}

	
	
}

