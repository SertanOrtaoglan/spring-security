package com.folksdev.security.jwt_token.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.folksdev.security.jwt_token.service.UserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	private final JwtAuthFilter jwtAuthFilter;
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	
	public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserService userService, PasswordEncoder passwordEncoder) {
		this.jwtAuthFilter = jwtAuthFilter;
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}
	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(x -> 
						x
								.requestMatchers("/auth/welcome/**", "/auth/addNewUser/**", "/auth/generateToken/**").permitAll()
								.requestMatchers("/auth/user/**").hasRole("USER")
								.requestMatchers("/auth/admin/**").hasRole("ADMIN")
						)
				.sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider())    //Aşağıda oluşturduğumuz 'authenticationProvider()'ı kullanıyoruz.(Önce alt satırdaki 'addFilterBefore()' çalışır ardından bu kod satırındaki 'authenticationProvider' çalışır.)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)   //authentication yapıldıktan sonra token'la birlikte gelen request'leri, bu 'jwtAuthFilter'(doFilterInternal) sayesinde validate ederiz.
				.build();
	}
	
	//"AuthenticationProvider", 'authentication' işlemini yaparken hangi servis ve hangi passwordEncoder'ı kullanacağımızı belirler.
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		return authenticationProvider;
	}
	
	//"AuthenticationManager", bizim 'authentication' işlemimizi yapacak.
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
	

}
