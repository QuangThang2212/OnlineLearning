package com.swp.onlineLearning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class OnlineLearningApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineLearningApplication.class, args);
	}

}
