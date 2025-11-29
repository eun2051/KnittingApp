package com.knittingapp.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String raw = "123456";
        String encoded = "$2a$10$7ZEmRcSqvL7Xwe5v6Kl2GOSSCaWfgyTizBNJWwmijp9DItkmNOFLq";
        System.out.println("matches 결과: " + encoder.matches(raw, encoded)); // true면 정상
    }
}
