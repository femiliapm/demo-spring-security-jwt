package com.project.demo.dto;

import java.util.Set;

public class JWTResponse {
	private String token;
	private String username;
	private String email;
	private Set<String> roles;
	private String type = "Bearer";
	public JWTResponse(String token, String username, String email, Set<String> roles) {
		super();
		this.token = token;
		this.username = username;
		this.email = email;
		this.roles = roles;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Set<String> getRoles() {
		return roles;
	}
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
