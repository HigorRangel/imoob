package com.imoob.hml.model;

import java.io.Serializable;
import java.util.Objects;

public class AuthenticationResponse implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String accessToken;
	
	public AuthenticationResponse() {
		
	}

	public AuthenticationResponse(String accessToken) {
		super();
		this.accessToken = accessToken;
	}

	public String getToken() {
		return accessToken;
	}

	public void setToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accessToken);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuthenticationResponse other = (AuthenticationResponse) obj;
		return Objects.equals(accessToken, other.accessToken);
	}
	
	
}
