package com.folksdev.security.jwt_token.model;

import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String username;
	private String password;
	
	private boolean accountNonExpired;
	private boolean isEnabled;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
	
	@ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
	@JoinTable(name = "authorities", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "role", nullable = false)
	@Enumerated(EnumType.STRING)
	private Set<Role> authorities;
	
	
}
