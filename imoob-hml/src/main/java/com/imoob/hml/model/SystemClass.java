package com.imoob.hml.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.imoob.hml.service.exceptions.GeneralException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_system_class")
@Getter
@Setter
@EqualsAndHashCode(exclude = "fields")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemClass implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Column(length = 50)
	private String classSimpleName;
	
	@NotNull
	@Column(length = 50)
	private String classNameTranslate;
	
	@NotNull
	@Column(length = 100)
	private String classFullName;
	
	@JsonIgnore
	@OneToMany(mappedBy = "systemClass")
	private Set<ClassField> fields = new HashSet<>();
	
	
	public Class<?> getSystemClass(){
		try {
			return Class.forName(this.classFullName);
		} catch (ClassNotFoundException e) {
			throw new GeneralException("Classe não encontrada.", SystemClass.class);
		}
	}
}
