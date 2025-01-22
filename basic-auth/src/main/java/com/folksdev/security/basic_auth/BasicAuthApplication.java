package com.folksdev.security.basic_auth;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.folksdev.security.basic_auth.dto.CreateUserRequest;
import com.folksdev.security.basic_auth.model.Role;
import com.folksdev.security.basic_auth.service.UserService;

@SpringBootApplication
public class BasicAuthApplication implements CommandLineRunner {
	
	public static void main(String[] args) {
		SpringApplication.run(BasicAuthApplication.class, args);
	}

	
	private final UserService userService;
	
	public BasicAuthApplication(UserService userService) {
		super();
		this.userService = userService;
	}
	
	
	//'CommandLineRunner'ı implemente ettik. Ve içerisinde bulunan 'run()' methodunu override ediyoruz. Böylelikle uygulama ayağa kalkarken aynı anda user oluşturma işlemini de gerçekleştireceğiz.
	@Override
	public void run(String... args) throws Exception {
		createDummyData();
	}
	
	//user yaratma işlemi(uygulama ayağa kalkarken bu user'lar otomatik olarak oluşacaktır.)
	private void createDummyData() {
		CreateUserRequest request1 = CreateUserRequest.builder()
				.name("Emin")
				.username("emin")
				.password("pass")
				.authorities(Set.of(Role.ROLE_USER))
				.build();
		userService.createUser(request1);
		
		
		CreateUserRequest request2 = CreateUserRequest.builder()
				.name("FSK")
				.username("fsk")
				.password("pass")
				.authorities(Set.of(Role.ROLE_FSK))
				.build();
		userService.createUser(request2);
		
		
		CreateUserRequest request3 = CreateUserRequest.builder()
				.name("No Name")
				.username("noname")
				.password("pass")
				.authorities(Set.of(Role.ROLE_ADMIN))
				.build();
		userService.createUser(request3);
	} 
	

}
