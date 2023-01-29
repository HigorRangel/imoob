package com.imoob.hml.service.utils.converters;

import com.imoob.hml.service.exceptions.DatabaseException;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CepConverter implements AttributeConverter<String, String>{

	@Override
	public String convertToDatabaseColumn(String attribute) {
		return attribute.replace("-", "");
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		if (dbData == null || dbData.isEmpty() || dbData.length() != 8) {
	        throw new DatabaseException("CEP não pode ser vazio, nulo ou ter tamanho diferente de 8 caracteres");
	    }
	    return dbData.substring(0, 5) + "-" + dbData.substring(5);
	}

}
