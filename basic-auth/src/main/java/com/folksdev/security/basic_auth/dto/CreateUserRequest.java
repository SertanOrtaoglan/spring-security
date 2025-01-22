package com.folksdev.security.basic_auth.dto;

import java.util.Set;

import com.folksdev.security.basic_auth.model.Role;

import lombok.Builder;

//Bu record, 'UserService' aracılığıyla yeni bir User oluşturmak için kullanılacaktır.
@Builder
public record CreateUserRequest(
		String name,
		String username,
		String password,
		Set<Role> authorities
		) {    

}
