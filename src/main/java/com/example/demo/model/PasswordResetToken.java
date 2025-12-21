package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="password_reset_token")
public class PasswordResetToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String token;
	
	@OneToOne
	@JoinColumn(name = "user_id" , nullable = false)
	private Users user;
	private LocalDateTime expiryTime;
	
	
	public PasswordResetToken() {}

	public PasswordResetToken( String token, Users user, LocalDateTime expiryTime) {
		
		this.token = token;
		this.user = user;
		this.expiryTime = expiryTime;
	}
	public String getToken() {
		return token;
	}

	public Users getUser() {
		return user;
	}

	public LocalDateTime getExpiryTime() {
		return expiryTime;
	}

	
}
