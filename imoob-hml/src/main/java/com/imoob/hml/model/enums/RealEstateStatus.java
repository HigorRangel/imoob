package com.imoob.hml.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum RealEstateStatus {
	@JsonProperty("A")
	ACTIVE('A'),

	@JsonProperty("I")
	INACTIVE('I');

	private Character value;

	private RealEstateStatus(char c) {
		this.value = c;
	}

	public Character getValue() {
		return value;
	}
	
	public static RealEstateStatus valueOf(Character status) {
		for(RealEstateStatus value : RealEstateStatus.values()) {
			if(value.getValue() == status) {
				return value;
			}
		}
		throw new IllegalArgumentException("O status digitado não foi encontrado.");
	}
}
