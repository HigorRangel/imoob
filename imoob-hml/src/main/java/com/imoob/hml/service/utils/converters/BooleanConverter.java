package com.imoob.hml.service.utils.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class BooleanConverter implements AttributeConverter<Boolean, String> {

	@Override
	public String convertToDatabaseColumn(Boolean attribute) {
		return attribute ? "S" : "N";

	}

	@Override
	public Boolean convertToEntityAttribute(String dbData) {
		return dbData != null && dbData.equals("S") ? true : false;
	}

}
