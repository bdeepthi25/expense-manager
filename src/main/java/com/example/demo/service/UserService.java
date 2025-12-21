package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.management.RuntimeErrorException;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ResetPasswordDTO;
import com.example.demo.dto.UserLoginDTO;
import com.example.demo.dto.UserRegisterDTO;
import com.example.demo.exception.DuplicateUserException;
import com.example.demo.exception.InvalidCredentialsException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.PasswordResetToken;
import com.example.demo.model.TokenBlacklist;
import com.example.demo.model.Users;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.repository.TokenBlacklistRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JWTUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@Service
public class UserService {

	@Autowired
	private  PasswordResetTokenRepository pwdResetRepo;
	private final UserRepository userRepo;
	private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    @Autowired
    private TokenBlacklistRepository blacklistRepo;
	public UserService(UserRepository userRepo, JWTUtil jwtUtil, AuthenticationManager authenticationManager) {
	
		this.userRepo = userRepo;
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
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
		
//		Users user = userRepo.findByEmail(loginDto.getEmail())
//				.orElseThrow( () -> new  UserNotFoundException("User Not found. Please Register") );
//		
//		boolean matches = passwordEncoder.matches(loginDto.getPassword(), user.getPassword());
//		
		
//		if(!matches)
//		{
//			throw new InvalidCredentialsException("Incorrect Password");
//		}
		
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
				);
		
	    // If authentication fails → exception thrown automatically
		return jwtUtil.generateToken(loginDto.getEmail()) ;
	}

	public ResponseEntity<?> logout(HttpServletRequest request) {
		// TODO Auto-generated method stub
		String authHeader = request.getHeader("Authorization");
		if(authHeader == null || !authHeader.startsWith("Bearer "))
		{
			return ResponseEntity.badRequest().body("No token found");
		}
		
		String token = authHeader.substring(7);
		blacklistRepo.save(new TokenBlacklist(token));
		
		return ResponseEntity.ok("Logged out successfully");
	}

	public ResponseEntity<?> forgotPassword(String email) 
	{
		Users user = userRepo.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found"));
		String resetToken = UUID.randomUUID().toString();
		
		PasswordResetToken token = new PasswordResetToken(
				resetToken,
				user,
				LocalDateTime.now().plusMinutes(15)
				);
		pwdResetRepo.save(token);
		return ResponseEntity.ok("Password reset link sent to email...Reset Token: "+ resetToken);
	}

	public ResponseEntity<?> resetPassword(ResetPasswordDTO resetPwdDto) {
		
		PasswordResetToken resetTokenInDB = pwdResetRepo.findByToken(resetPwdDto.getToken())
				.orElseThrow(() -> new RuntimeException("InvalidToken"));
		if(resetTokenInDB.getExpiryTime().isBefore(LocalDateTime.now()))
		{
			throw new RuntimeException("Token expired");
		}
		
		Users user = resetTokenInDB.getUser();
		user.setPassword( passwordEncoder.encode(resetPwdDto.getNewPassword()));
		userRepo.save(user);
		
		pwdResetRepo.delete(resetTokenInDB);
		return ResponseEntity.ok("Password Reset Successful");
	}
	
	
	
}
