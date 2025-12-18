package com.example.demo.security;

import java.io.IOException;
import java.net.Authenticator.RequestorType;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthFilter extends OncePerRequestFilter{

	private final JWTUtil jwtUtil;
	private final CustomUserDetailsService  userDetailsService ;
	
	public JWTAuthFilter(JWTUtil jwtUtil, CustomUserDetailsService userDetailsService) {

		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException 
	{
		String authHeader = request.getHeader("Authorization");
		if(authHeader != null && authHeader.startsWith("Bearer "))
		{
			String token = authHeader.substring(7);
			if( jwtUtil.validateToken(token))
			{
				String email = jwtUtil.extractEmail(token);
				var userDetail =  userDetailsService.loadUserByUsername(email);
				UsernamePasswordAuthenticationToken authentication = 
						new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
				
				authentication.setDetails(
						new WebAuthenticationDetailsSource()
							.buildDetails(request)
						);
				
				SecurityContextHolder.getContext()
						.setAuthentication(authentication);
			}
		}
		
		 filterChain.doFilter(request, response);
		
	}
	
}
