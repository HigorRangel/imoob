package com.imoob.hml.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserStatus  {
    @JsonProperty("A")
	ACTIVE('A'),
	
	@JsonProperty("I")
	INACTIVE('I'),
	
	@JsonProperty("P")
	PENDING('P'),
	
	@JsonProperty("B")
	BLOCKED('B'),
	
	@JsonProperty("E")
	EXPIRED('E');
	
	private char statusKey;
	
	
	private UserStatus(char statusKey) {
		this.statusKey = statusKey;
	}
	
	public char getStatusKey() {
		return statusKey;
	}
	
	public static UserStatus valueOf(char statusKey) {
		for(UserStatus value : UserStatus.values()) {
			if(value.getStatusKey() == statusKey) {
				return value;
			}
		}
		throw new IllegalArgumentException("Invalid UserStatus code.");
	}
	
}
