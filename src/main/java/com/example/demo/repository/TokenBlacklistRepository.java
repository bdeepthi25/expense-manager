package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.TokenBlacklist;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist,Long>{

	boolean existsByToken(String token);
}
