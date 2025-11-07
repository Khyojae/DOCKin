package com.example.DOCKin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DocKinApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocKinApplication.class, args);
	}

}
