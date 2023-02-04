package com.imoob.hml.service.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = RealEstateStatusValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface RealEstateValue {
	String message() default "O status digitado não foi encontrado.";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}
