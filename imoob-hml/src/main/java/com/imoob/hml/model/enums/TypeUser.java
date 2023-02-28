package com.imoob.hml.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TypeUser {
	@JsonProperty("F")
	FUNCIONARIO("Funcionário",'F'),
	
	@JsonProperty("C")
	CLIENTE("Cliente", 'C');
	
	private String name;
	private Character acronym;
	
	private TypeUser(String name, Character acronym) {
		this.name = name;
		this.acronym = acronym;
	}

	public String getName() {
		return name;
	}

	public Character getAcronym() {
		return acronym;
	}
	
	public static TypeUser valueOf(char acronym) {
		for(TypeUser value : TypeUser.values()) {
			if(value.getAcronym() == acronym) {
				return value;
			}
		}
		throw new IllegalArgumentException("Invalid UserStatus code.");
	}

}
