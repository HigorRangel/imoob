package com.imoob.hml.service.exceptions;

public class ResourceNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(Object id, Class<?> resourceClass) {
		super(resourceClass.getSimpleName() + " não encontrado. Id " + id);
	}
}
