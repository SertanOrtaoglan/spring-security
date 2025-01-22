package com.folksdev.security.basic_auth.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.folksdev.security.basic_auth.dto.CreateUserRequest;
import com.folksdev.security.basic_auth.model.User;
import com.folksdev.security.basic_auth.repository.UserRepository;

@Service
public class UserService {
	
	private final UserRepository userRepository;
	
	private final BCryptPasswordEncoder passwordEncoder; 

	public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	//Bu methodu 'UserDetailsService' implementasyonunda(UserDetailsServiceImpl) çağıracağız. ["loadUserByUsername()" methodunda]
	public Optional<User> getByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	//User oluşturabilmek için 'createUser' methodu yazarız. Bu methodu API aracılığıyla kullanacağız.
	public User createUser(CreateUserRequest request) {
		
		User newUser = User.builder()
				.name(request.name())
				.username(request.username())
				.password(passwordEncoder.encode(request.password()))
				.authorities(request.authorities())
				.accountNonExpired(true)
				.credentialsNonExpired(true)
				.isEnabled(true)
				.accountNonLocked(true)
				.build();
		
		return userRepository.save(newUser);
	}
	
	

}
