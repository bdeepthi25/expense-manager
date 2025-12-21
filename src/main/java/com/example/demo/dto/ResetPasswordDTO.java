package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class ResetPasswordDTO {

	@NotBlank
	private String token;

	@NotBlank
	private String newPassword;

	public String getToken() {
		return token;
	}

	public String getNewPassword() {
		return newPassword;
	}
}
