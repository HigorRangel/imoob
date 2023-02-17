package com.imoob.hml.model.systemClass;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.imoob.hml.model.ClassField;
import com.imoob.hml.model.SystemClass;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SystemClassDTO {

	private Long id;
	private String classSimpleName;
	private String classNameTranslate;
	private String classFullName;
	
	private Set<ClassField> fields = new HashSet<>();

	public SystemClassDTO(SystemClass systemClass) {
		super();
		this.id = systemClass.getId();
		this.classSimpleName = systemClass.getClassSimpleName();
		this.classNameTranslate = systemClass.getClassNameTranslate();
//		this.fields = system;
	}
	
	
}
