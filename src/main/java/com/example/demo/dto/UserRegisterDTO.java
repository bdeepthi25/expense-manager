package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserRegisterDTO {
	
	@NotBlank(message = "username must be filled")
	private String username;
	
	@Email(regexp= "^[a-zA-Z0-9._%+-]+@[a-zA-Z.-]+\\.[a-zA-Z]{2,6}$"
			,message = "Please provide a valid email address")
	@NotBlank(message="Email can not be empty")
	private String email;
	
	@NotBlank(message="Password can not be empty")
	private String password;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
