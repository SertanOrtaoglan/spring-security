package com.folksdev.security.in_memory.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity                 //SecurityFilterChain'i uygulamak için kullanırız.
@EnableMethodSecurity              //Path yerine direkt controller class'ını verecek olursak o controller class'ı içerisindeki methodların security'sini sağlar.
public class SecurityConfig {
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	public UserDetailsService users() {
		UserDetails user1 = User.builder()
				.username("fsk")
				.password(bCryptPasswordEncoder().encode("pass"))
				.roles("USER")
				.build();
				
		UserDetails admin = User.builder()
				.username("enesErgen")
				.password(bCryptPasswordEncoder().encode("pass"))
				.roles("ADMIN")
				.build();
		
		return new InMemoryUserDetailsManager(user1, admin);
	}
	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
		
		security
				.headers(x -> x.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))           //X-Frame-Options header'i "disable" etme(method reference ile)
				.csrf(AbstractHttpConfigurer::disable)                                                 //csrf'i devre dışı bırakma
				.formLogin(AbstractHttpConfigurer::disable)                                            //login form'u sayfasını iptal ettik.(username ve password'ü, postman'deki header'la göndereceğiz) Açmak için 'formLogin(Customizer.withDefaults()' dememiz gerekir.
				.authorizeHttpRequests(x -> x.requestMatchers("/public/**", "/auth/**").permitAll())   //Bu iki endpoint'e herkes ulaşabilir demektir.
				.authorizeHttpRequests(x -> x.requestMatchers("/private/user/**").hasRole("USER"))     //Yazdığımız path'e[/private/user/**] sadece 'USER' rolünde olanlar girebilir. Başka rolde olan biri girmeye çalışırsa '403(Forbidden)' alacaktır.
				.authorizeHttpRequests(x -> x.requestMatchers("/private/admin/**").hasRole("ADMIN"))   //Yazdığımız path'e[/private/admin/**] sadece 'ADMIN' rolünde olanlar girebilir. Başka rolde olan biri girmeye çalışırsa '403(Forbidden)' alacaktır.
				.authorizeHttpRequests(x -> x.anyRequest().authenticated())							   //Yukarıdaki yazdığımız iki endpoint hariç["/public/**" ve "/auth/**"] diğer tüm endpoint'lere ulaşmak için authenticated olmamız gerekir.
				.httpBasic(Customizer.withDefaults());                                                 //login web page[pop-up(açılır pencere)]'i göstermek için bu methodu yazarız.
				
		return security.build();
	}
	

}
