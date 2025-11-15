import org.gradle.kotlin.dsl.compileOnly
import org.gradle.kotlin.dsl.implementation
import org.gradle.api.tasks.testing.Test

plugins {
    java
    // Spring Boot 3.5.7 버전으로 통일
    id("org.springframework.boot") version "3.5.7"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
    toolchain {
        // Java 21 사용
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
    // ----------------------------------------
    // CORE (Spring Boot Starter)
    // ----------------------------------------

    // Web 환경 (WebFlux와 Web을 모두 사용하고 계시지만, 보통 하나만 사용합니다. 둘 다 추가했습니다.)
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // DB 및 ORM/SQL
    implementation("org.springframework.boot:spring-boot-starter-data-jpa") // JPA
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.3") // MyBatis

    // Validation, Security, WebSocket, Template
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // ----------------------------------------
    // UTILITY & EXTERNAL
    // ----------------------------------------

    // Lombok (중복 제거 및 올바른 Kotlin DSL 문법 사용)
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Spring Security + Thymeleaf 연동
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")

    // Firebase Admin
    implementation("com.google.firebase:firebase-admin:9.2.0")

    // JWT (Json Web Token)
    implementation("io.jsonwebtoken:jjwt-api:0.11.5") // API
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5") // 구현체
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5") // JSON 파서

    // ----------------------------------------
    // DATABASE DRIVERS
    // ----------------------------------------

    // MySQL DB 연결
    runtimeOnly("com.mysql:mysql-connector-j")

    // H2 In-Memory DB (개발/테스트용)
    runtimeOnly("com.h2database:h2")

    // ----------------------------------------
    // TEST
    // ----------------------------------------

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// JUnit 5 (Platform) 사용 설정
tasks.withType<Test> {
    useJUnitPlatform()
}