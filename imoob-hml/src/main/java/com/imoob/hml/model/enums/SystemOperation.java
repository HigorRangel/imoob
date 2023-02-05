package com.imoob.hml.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SystemOperation {
	@JsonProperty("I")
    INSERT('I'),
    
    @JsonProperty("R")
    REMOVE('R'),
    
    @JsonProperty("M")
    MODIFY('M'),
    
    @JsonProperty("E")
    ENABLE('E'),
    
    @JsonProperty("D")
    DISABLE('D');

    private final Character value;

    SystemOperation(Character value) {
        this.value = value;
    }

    public Character getName() {
        return value;
    }

    public static SystemOperation valueOf(Character value) {
        for (SystemOperation operation : SystemOperation.values()) {
            if (operation.getName().equals(value)) {
                return operation;
            }
        }
        throw new IllegalArgumentException("A operação " + value + " não foi encontrada.");
    }
}
