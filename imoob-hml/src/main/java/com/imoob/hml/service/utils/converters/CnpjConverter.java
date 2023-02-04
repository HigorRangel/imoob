package com.imoob.hml.service.utils.converters;

import com.imoob.hml.service.utils.PersonalDataUtils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CnpjConverter implements AttributeConverter<String, String> {

	@Override
	public String convertToDatabaseColumn(String attribute) {
		return PersonalDataUtils.cnpjRemoveSymbols(attribute);
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		return PersonalDataUtils.cnpjPutSymbols(dbData);
	}

}
