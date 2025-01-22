package com.folksdev.security.jwt_token.dto;

import java.util.Set;

import com.folksdev.security.jwt_token.model.Role;

import lombok.Builder;

@Builder
public record CreateUserRequest(
		String name,
		String username,
		String password,
		Set<Role> authorities
		) {

}
