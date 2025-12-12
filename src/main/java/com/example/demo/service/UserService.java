package com.example.demo.service;

import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UserLoginDTO;
import com.example.demo.dto.UserRegisterDTO;
import com.example.demo.exception.DuplicateUserException;
import com.example.demo.exception.InvalidCredentialsException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.Users;
import com.example.demo.repository.UserRepository;

import jakarta.validation.Valid;

@Service
public class UserService {

	private final UserRepository userRepo;

	public UserService(UserRepository userRepo) {
	
		this.userRepo = userRepo;
	}
 
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	public @Nullable Object register(@Valid UserRegisterDTO regDto) {
		
		boolean exists = userRepo.existsByEmail(regDto.getEmail());
		if(exists)
		{
			throw new DuplicateUserException("Email already registered");
		}
		
		Users newUser = new Users();
		newUser.setEmail(regDto.getEmail());
		newUser.setUsername(regDto.getUsername());
		
		String hashedPassword = passwordEncoder.encode(regDto.getPassword());
		newUser.setPassword(hashedPassword);
		
		userRepo.save(newUser);
		return "User REgistered Successfully";
	}

	public String login(@Valid UserLoginDTO loginDto) {
		
		Users user = userRepo.findByEmail(loginDto.getEmail())
				.orElseThrow( () -> new  UserNotFoundException("User Not found. Please Register") );
		
		boolean matches = passwordEncoder.matches(loginDto.getPassword(), user.getPassword());
		
		
		if(!matches)
		{
			throw new InvalidCredentialsException("Incorrect Password");
		}
		return "Login Successful";
	}
	
	
	
}
