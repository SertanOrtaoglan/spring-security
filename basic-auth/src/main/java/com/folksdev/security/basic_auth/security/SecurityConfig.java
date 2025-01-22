package com.folksdev.security.basic_auth.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.folksdev.security.basic_auth.model.Role;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity security, HandlerMappingIntrospector introspector) throws Exception {
		
		MvcRequestMatcher.Builder mvcRequestBuilder = new MvcRequestMatcher.Builder(introspector);    //Sadece "ADMIN"in, H2 Console'a ulaşmasını istiyoruz. Bu yüzden 'MvcRequestMatcher.Builder'dan yararlanacağız.
		
		security
				.headers(x -> x.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
				.csrf(csrfConfig -> csrfConfig.ignoringRequestMatchers(mvcRequestBuilder.pattern("/public/**"))
											  .ignoringRequestMatchers(PathRequest.toH2Console()))
				.authorizeHttpRequests(x -> 
							x		
									.requestMatchers(mvcRequestBuilder.pattern("/public/**")).permitAll()
									.requestMatchers(mvcRequestBuilder.pattern("/private/admin/**")).hasRole(Role.ROLE_ADMIN.getValue())  //Bu path'e sadece "ADMIN"ler giriş yapabilsin istiyoruz.
									.requestMatchers(mvcRequestBuilder.pattern("/private/**")).hasAnyRole(Role.ROLE_USER.getValue(), Role.ROLE_ADMIN.getValue(), Role.ROLE_FSK.getValue())
									.requestMatchers(PathRequest.toH2Console()).hasRole("ADMIN")   //Sadece "ADMIN", H2 console'a ulaşsın diye bu kodu ekleriz.
									.anyRequest().authenticated()
				)
				.formLogin(Customizer.withDefaults())    //"session" süresini ayarladıktan sonra deneme yapmak için login web page'i açarız. Tekrar kapatmak için "formLogin(AbstractHttpConfigurer::disable)" dememiz gerekir.
				.httpBasic(Customizer.withDefaults())    //pop-up(açılır pencere) bir login page getirir.
				.sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));    //"session" süresini ayarlamak için bu kodu ekleriz. Daha sonra application.properties'te süreyi ayarlarız.
		
		
		return security.build();
		
	}
	

}
