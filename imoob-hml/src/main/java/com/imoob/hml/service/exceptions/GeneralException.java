package com.imoob.hml.service.exceptions;

public class GeneralException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public GeneralException(String message) {
		super(message);
	}

	public GeneralException(String className, Class<?> objClass) {
		super("[" + objClass.getName() + "] " + className);
	}
	
	public GeneralException(String message, Throwable e) {
		super(message, e);
	}

}
