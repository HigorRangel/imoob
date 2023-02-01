package com.imoob.hml.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum RealStateStatus {
	@JsonProperty("A")
	  ACTIVE("A"),

	  @JsonProperty("I")
	  INACTIVE("I");

	  private String value;

	  private RealStateStatus(String value) {
	    this.value = value;
	  }

	  public String getValue() {
	    return value;
	  }
}
