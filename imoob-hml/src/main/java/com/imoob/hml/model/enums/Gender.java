package com.imoob.hml.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Gender {
	@JsonProperty("M")
	MASCULINO("Masculino"),

	@JsonProperty("F")
	FEMININO("Feminino"),

	@JsonProperty("O")
	OUTRO("Outro");

	private String name;

	Gender(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static Gender fromValue(String value) {
		for (Gender g : values()) {
			if (g.toString().equals(value)) {
				return g;
			}
		}
		throw new IllegalArgumentException("Gênero inválido: " + value);
	}
}
