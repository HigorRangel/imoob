package com.imoob.hml.service.validators;

import com.imoob.hml.model.enums.RealEstateStatus;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RealEstateStatusValidator implements ConstraintValidator<RealEstateValue, Character>{

	@Override
	public boolean isValid(Character value, ConstraintValidatorContext context) {
		if(value == null) {
			return true;
		}
		
		try {
			RealEstateStatus.valueOf(value);
			return true;
		}
		catch (IllegalArgumentException e) {
			return false;
		}
	}

	

}
