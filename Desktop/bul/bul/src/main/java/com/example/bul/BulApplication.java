package com.example.bul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BulApplication {

	public static void main(String[] args) {
		SpringApplication.run(BulApplication.class, args);
	}

}
