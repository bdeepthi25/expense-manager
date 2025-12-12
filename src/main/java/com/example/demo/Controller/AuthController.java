package com.example.demo.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserLoginDTO;
import com.example.demo.dto.UserRegisterDTO;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    AuthController(UserService userService) {
        this.userService = userService;
    }
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody UserRegisterDTO regDto)
	{
		return ResponseEntity.ok(userService.register(regDto));
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO loginDto)
	{
		String response = userService.login(loginDto);
		return ResponseEntity.ok(response);
	}
}
