plugins {
	java
	id("org.springframework.boot") version "3.5.7"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("com.google.firebase:firebase-admin:9.2.0")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5") // API
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5") // 구현체
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5") // JSON 파서
	// MySQL DB 연결을 위한 드라이버 추가
	runtimeOnly("com.mysql:mysql-connector-j")
	implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.3")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("com.h2database:h2")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
