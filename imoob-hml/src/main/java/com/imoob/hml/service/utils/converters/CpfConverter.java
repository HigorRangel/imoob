package com.imoob.hml.service.utils.converters;

import java.text.DecimalFormat;

import com.imoob.hml.service.exceptions.GeneralException;
import com.imoob.hml.service.utils.StringUtils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CpfConverter implements AttributeConverter<String, String>{

	@Override
	public String convertToDatabaseColumn(String attribute) {
		return attribute.replace(".", "").replace("-", "");
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		
		if(!StringUtils.isNullOrEmpty(dbData) && StringUtils.isOnlyNumbers(dbData) && dbData.length() == 11) {
			String cpfFormatted = dbData.substring(0, 3) + "." + dbData.substring(3, 6) + "." + dbData.substring(6, 9) + "-" + dbData.substring(9, 11);

			return cpfFormatted;
		}
		throw new GeneralException("O CPF não foi inserido corretamente. Insira somente números.");		
	}

}
