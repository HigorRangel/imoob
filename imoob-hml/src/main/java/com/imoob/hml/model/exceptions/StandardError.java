package com.imoob.hml.model.exceptions;

import java.io.Serializable;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class StandardError implements Serializable {

	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;
	@JsonDeserialize(using = InstantDeserializer.class)
	@JsonSerialize(using = InstantSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
	private Instant timestamp;

	private Integer status;

	private String error;

	private String message;
	
	private String errorMessage;

	private String path;

	private String className;

	private String attributeName;

	public StandardError(Instant now, Integer status, String error, String message, String path) {
		this.timestamp = now;
		this.status = status;
		this.message = message;
		this.path = path;
	}

	public StandardError(Instant timestamp, Integer status, String error, String message, String path,
			String errorMessage) {
		this.timestamp = timestamp;
		this.status = status;
		this.message = message;
		this.path = path;
		this.errorMessage = errorMessage;
	}

	public StandardError(Instant timestamp, Integer status, String error, String message, Throwable cause, String path) {
		this.timestamp = timestamp;
		this.status = status;
		this.message = message;
		this.path = path;
		this.errorMessage = (cause != null ? cause.getMessage() : null);
	}

}
