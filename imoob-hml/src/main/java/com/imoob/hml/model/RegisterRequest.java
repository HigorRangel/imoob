package com.imoob.hml.model;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;	

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RegisterRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String firstName;
	private String middleNames;
	private String lastName;
	private String email;
	private String cpf;
	private String cepAddress;
	private String numberAddress;
	private String complementAddress;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate birthDate;
		
	private String password;
	
	
}
