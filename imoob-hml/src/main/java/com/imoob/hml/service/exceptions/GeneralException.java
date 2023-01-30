package com.imoob.hml.service.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneralException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	private Class<?> classObject;
	private String attributeName;
	
	public GeneralException(String message) {
		super(message);
	}

	public GeneralException(String className, Class<?> objClass) {
		super("[" + objClass.getSimpleName() + "] " + className);
	}
	
	public GeneralException(String message, Throwable e) {
		super(message, e);
	}
	
	public GeneralException(String message, Throwable e, Class<?> objClass) {
		super(message, e);
		this.classObject = objClass;
	}

}
