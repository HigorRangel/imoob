package com.imoob.hml.service.utils.converters;

import com.imoob.hml.service.exceptions.GeneralException;
import com.imoob.hml.service.utils.StringUtils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CnpjConverter implements AttributeConverter<String, String> {

	@Override
	public String convertToDatabaseColumn(String attribute) {
		return attribute.replace(".", "").replace("-", "").replace("/", "");
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		dbData = convertToDatabaseColumn(dbData);

		if (!StringUtils.isNullOrEmpty(dbData) && StringUtils.isOnlyNumbers(dbData) && dbData.length() == 14) {
			return dbData.replaceFirst("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");

		}
		throw new GeneralException("O CNPJ não foi inserido corretamente. Deve ter 14 dígitos, sendo somente números.");
	}

}
