package com.folksdev.security.jwt_token.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.folksdev.security.jwt_token.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByUsername(String userName);

}
