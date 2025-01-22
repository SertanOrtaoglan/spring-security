package com.folksdev.security.jwt_token.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.folksdev.security.jwt_token.service.JwtService;
import com.folksdev.security.jwt_token.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	
	private final JwtService jwtService;
	private final UserService userService;
	
	
	public JwtAuthFilter(JwtService jwtService, UserService userService) {
		this.jwtService = jwtService;
		this.userService = userService;
	}

	//JwtAuthFilter'ın tek bir görevi vardır. O da bu "doFilterInternal()" methodu'dur. Bu method, gelen request içerisindeki token'ı validate etmeye yarar. 
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String userName = null;
		
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			userName = jwtService.extractUser(token);
		}
		
		if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails user = userService.loadUserByUsername(userName);
			if(jwtService.validateToken(token, user)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		
		
		filterChain.doFilter(request, response);
		
	}
	
	

}
