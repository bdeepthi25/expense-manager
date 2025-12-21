package com.example.demo.security;

import java.io.IOException;
import java.net.Authenticator.RequestorType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.model.TokenBlacklist;
import com.example.demo.repository.TokenBlacklistRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthFilter extends OncePerRequestFilter{

	private final JWTUtil jwtUtil;
	private final CustomUserDetailsService  userDetailsService ;
	@Autowired
	private TokenBlacklistRepository blacklistRepo;
	public JWTAuthFilter(JWTUtil jwtUtil, CustomUserDetailsService userDetailsService) {

		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException 
	{
		System.out.println("🧪 JWT FILTER HIT: " + request.getServletPath());

		String path = request.getServletPath();

        // 🔥 SKIP AUTH APIs (MOST IMPORTANT FIX)
		if (
			    path.startsWith("/api/auth/login") ||
			    path.startsWith("/api/auth/register") ||
			    path.startsWith("/api/auth/forgot-password") ||
			    path.startsWith("/api/auth/reset-password")
			) {
			    filterChain.doFilter(request, response);
			    return;
			}
		String authHeader = request.getHeader("Authorization");
		if(authHeader != null && authHeader.startsWith("Bearer "))
		{
			String token = authHeader.substring(7);
			
			if(blacklistRepo.existsByToken(token))
			{
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("Token has been logged out");
				return;
			}
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
