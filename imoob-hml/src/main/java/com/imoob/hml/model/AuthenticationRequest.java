package com.imoob.hml.model;

import java.io.Serializable;

public class AuthenticationRequest implements Serializable{
	

	private static final long serialVersionUID = 1L;
	private String email;
	private String password;
	
	public AuthenticationRequest() {
		
	}

	public AuthenticationRequest(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
