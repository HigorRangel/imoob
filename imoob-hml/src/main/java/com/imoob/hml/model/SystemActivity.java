package com.imoob.hml.model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_system_activity")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SystemActivity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY  )
	private Long id;
	
	
	private Long objectId;
	
	@Column(length = 50)
	private String path;
	
	private String operation;
	
	@Column(length = 256)
	private String description;

	private Integer statusCode;
	
	@Column(length = 256)
	private String errorMessage;
	
	
	@Column(length = 512)
	private String stackTrace;
	
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
	private Instant timestamp;
		
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	
//	public static class SystemActivityBuilder{
//		Character operation;
//		
//		public SystemActivityBuilder operation(SystemOperation operation) {
//			this.operation = operation.getName();
//			return this;
//		}
//	}
}
