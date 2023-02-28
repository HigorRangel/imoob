package com.imoob.hml.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MaritalStatus {
	@JsonProperty("S")
	SOLTEIRO("Solteiro(a)", "S"),

	@JsonProperty("C")
	CASADO("Casado(a)", "C"),

	@JsonProperty("D")
	DIVORCIADO("Divorciado(a)", "D"),

	@JsonProperty("V")
	VIUVO("Viúvo(a)", "V"),

	@JsonProperty("U")
	UNIAO_ESTAVEL("União Estável", "U");

	private final String name;
	private final String code;

	private MaritalStatus(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public static MaritalStatus fromCode(String code) {
		for (MaritalStatus status : values()) {
			if (status.code.equals(code)) {
				return status;
			}
		}
		throw new IllegalArgumentException("Invalid marital status code: " + code);
	}
}
