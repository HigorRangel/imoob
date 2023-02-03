package com.imoob.hml.model;

import java.io.Serializable;
import java.time.Instant;

import org.hibernate.validator.constraints.br.CNPJ;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.imoob.hml.model.enums.RealStateStatus;
import com.imoob.hml.service.utils.converters.CnpjConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
import lombok.ToString;

@Entity
@Builder
@ToString
@Table(name = "tb_real_estate")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RealEstate implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	@Setter
	private Long id;
	
	@NotNull
	@CNPJ
	@Column(length = 14)
	@Convert(converter = CnpjConverter.class)
	@Getter
	@Setter
	private String cnpj;

	@NotNull
	@Column(length = 100)
	@Getter
	@Setter
	private String corporateName;

	@NotNull
	@Column(length = 100)
	@Getter
	@Setter
	private String tradingName;

	

	@NotNull
	@Getter
	@Setter
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
	private Instant created;

	@NotNull
	@Getter
	@Setter
	@Email
	@Column(length = 100)
	private String businessEmail;
	
	@NotNull
	@Getter
	@Setter
	@Column(length = 100)
	private String website;
	
	
	@NotNull
	private Character status;

	// @JsonIgnore
//	public Set<User> getUsers(){
//		Set<User> set = new HashSet<User>();
//		for(UserRole  x: users) {
//			set.add(x.getUser());
//		}
//		return set;
//	}
//	
	public RealStateStatus getStatus() {
		return RealStateStatus.valueOf(this.status);
	}
	
	public void setStatus(RealStateStatus status) {
		this.status = status.getValue();
	}
//
//	public RealEstate(@NotNull @CNPJ String cnpj, @NotNull String corporateName, @NotNull String tradingName,
//			@NotNull String status, @NotNull Instant created, @NotNull @Email String businessEmail,
//			@NotNull String website) {
//		super();
//		this.cnpj = cnpj;
//		this.corporateName = corporateName;
//		this.tradingName = tradingName;
//		this.status = status;
//		this.created = created;
//		this.businessEmail = businessEmail;
//		this.website = website;
//	}
//	
//	

}
