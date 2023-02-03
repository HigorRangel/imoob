package com.imoob.hml.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum RealStateStatus {
	@JsonProperty("A")
	ACTIVE('A'),

	@JsonProperty("I")
	INACTIVE('I');

	private Character value;

	private RealStateStatus(char c) {
		this.value = c;
	}

	public Character getValue() {
		return value;
	}
	
	public static RealStateStatus valueOf(Character status) {
		for(RealStateStatus value : RealStateStatus.values()) {
			if(value.getValue() == status) {
				return value;
			}
		}
		throw new IllegalArgumentException("Invalid UserStatus code.");
	}
}
