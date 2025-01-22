package com.folksdev.security.basic_auth.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.folksdev.security.basic_auth.model.User;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserService userService;
	
	public UserDetailsServiceImpl(UserService userService) {
		this.userService = userService;
	}

	//Security tarafında bu methodu kullanacağız.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userService.getByUsername(username);
		return user.orElseThrow(EntityNotFoundException::new);
	}
	

}
