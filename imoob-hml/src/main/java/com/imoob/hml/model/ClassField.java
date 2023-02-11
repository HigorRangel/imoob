package com.imoob.hml.model;

import java.io.Serializable;

import com.imoob.hml.service.exceptions.GeneralException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_class_field")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class ClassField implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Column(length = 20)
	private String fieldName;
	
	@NotNull
	@Column(length = 20)
	private String fieldNameTranslate;
	
	@Column(length = 100)
	private String fieldType;
	
	@ManyToOne
	@JoinColumn(name = "systemClass_id")
	private SystemClass systemClass;
	
	
	public Class<?> getFieldType(){
		try {
			return Class.forName(this.fieldName);
		}
		catch (ClassNotFoundException e) {
			throw new GeneralException("A classe não foi encontrada.", Field.class);
		}
	}
	
	public void setFieldType(Class<?> fieldName) {
		this.fieldName = fieldName.getName();
	}
	
	
}
