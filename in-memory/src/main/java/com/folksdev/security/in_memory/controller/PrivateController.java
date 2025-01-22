package com.folksdev.security.in_memory.controller;

//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private")
public class PrivateController {
	
	@GetMapping
	public String helloWorldPrivate() {
		return "Hello World! from private endpoint";
	}
	
	
	//"USER" için endpoint yaratma
	//@PreAuthorize("hasRole('USER')")    //Bu işlemi burada değil de SecurityFilterChain'in içerisinde de yapabiliriz.
	@GetMapping("/user")
	public String helloWorldUserPrivate() {
		return "Hello World! from user private endpoint";
	}
	
	//"ADMIN" için endpoint yaratma
	//@PreAuthorize("hasRole('ADMIN')")   //Bu işlemi burada değil de SecurityFilterChain'in içerisinde de yapabiliriz.
	@GetMapping("/admin")
	public String helloWorldAdminPrivate() {
		return "Hello World! from admin private endpoint";
	}
	

}
