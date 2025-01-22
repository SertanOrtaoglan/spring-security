package com.folksdev.security.basic_auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.folksdev.security.basic_auth.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByUsername(String userName);   //Bu methodu kendi yazacağımız 'UserDetailsServiceImpl'da kullanacağız.

}
