package com.example.demo.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long>{

	
	boolean existsByEmail(String email);
	
	public Optional<Users> findByEmail(String email);

	Optional<Users> findByUsername(String username);
	
}
