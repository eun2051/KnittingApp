package com.knittingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 뜨개질 프로젝트 관리 앱의 메인 클래스입니다.
 * Spring Boot 애플리케이션의 진입점 역할을 합니다.
 */
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
