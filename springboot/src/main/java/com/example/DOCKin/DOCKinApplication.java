package com.example.DOCKin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
public class DOCKinApplication {

	public static void main(String[] args) {
		SpringApplication.run(DOCKinApplication.class, args);
	}

}
