package com.imoob.hml.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ApiOperation {
	@JsonProperty("POST")
	POST("POST"),

	@JsonProperty("PUT")
	PUT("PUT"),

	@JsonProperty("PATCH")
	PATCH("PATCH"),

	@JsonProperty("DELETE")
	DELETE("DELETE"),

	@JsonProperty("GET")
	GET("GET");

	private final String value;

	ApiOperation(String value) {
		this.value = value;
	}

	public String getName() {
		return value;
	}

	public static ApiOperation getByName(String value) {
		for (ApiOperation operation : ApiOperation.values()) {
			if (operation.getName().equals(value)) {
				return operation;
			}
		}
		throw new IllegalArgumentException("A operação " + value + " não foi encontrada.");
	}
}
