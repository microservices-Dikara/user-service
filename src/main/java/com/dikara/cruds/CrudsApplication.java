package com.dikara.cruds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
//@EnableMethodSecurity
public class CrudsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudsApplication.class, args);
	}

}
