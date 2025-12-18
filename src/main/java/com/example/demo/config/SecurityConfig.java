package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.security.JWTAuthFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	  @Autowired
	    private BCryptPasswordEncoder passwordEncoder;

	  @Autowired
	  private JWTAuthFilter jwtAuthFilter;
	  
	  @Autowired
	  private CustomUserDetailsService customUserDetailsService;
	  
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session ->
        		session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/auth/register", "/api/auth/login")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//            .formLogin(form -> form.disable()); // disable default Spring login form

        return http.build();
        
       
    }
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
    	AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        				builder.userDetailsService(customUserDetailsService)
        						.passwordEncoder(passwordEncoder);
          return builder.build();
    }
}
