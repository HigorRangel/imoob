package com.imoob.hml.model.exceptions;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ValidationException implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonDeserialize(using = InstantDeserializer.class)
	@JsonSerialize(using = InstantSerializer.class) 
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
	private Instant timestamp;
	private Integer status;
//	private String error;
//	private String message;
	private String path;
	
	private List<Map<String, String>>constraints;
	
	
	
}
