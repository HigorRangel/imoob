package com.imoob.hml.service.exceptions;

import com.imoob.hml.model.enums.SystemOperation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneralException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	private Class<?> classObject;
	private String attributeName;
	
	private String operation;
	
	private Object object;

	
	public GeneralException(String message) {
		super(message);
	}
	
	public GeneralException(String message, String operation) {
		super(message);
		this.operation = operation;
	}

	public GeneralException(String className, Object obj) {
		super("[" + obj.getClass().getSimpleName() + "] " + className);

		this.object = obj;
		this.classObject = obj.getClass();
	}
	
	public GeneralException(String className, Class<?> objClass, String operation) {
		super("[" + objClass.getSimpleName() + "] " + className);
		this.operation = operation;
	}
	
	public GeneralException(String message, Throwable e) {
		super(message, e);
	}
	
	public GeneralException(String message, Throwable e, Object object) {
		super(message, e);
		this.classObject = object.getClass();
		this.object = object;
	}

}
