package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name ="TokenBlacklist")
public class TokenBlacklist {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 500, nullable = false, unique = true)
	private String token;
	
	private LocalDateTime blacklistedAt = LocalDateTime.now();

	public TokenBlacklist(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	
}
