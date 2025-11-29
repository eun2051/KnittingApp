package com.knittingapp.controller;

import com.knittingapp.dto.UserRegisterRequestDTO;
import com.knittingapp.dto.UserLoginRequestDTO;
import com.knittingapp.domain.User;
import com.knittingapp.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 회원가입/로그인 API 컨트롤러
 * 모든 응답/설명은 한국어로 작성
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /** 회원가입 */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody UserRegisterRequestDTO dto) {
        User user = userService.register(dto);
        
        Map<String, Object> userData = new java.util.HashMap<>();
        userData.put("id", user.getId());
        userData.put("email", user.getEmail());
        userData.put("nickname", user.getNickname());
        
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("status", 200);
        response.put("code", "REGISTER_SUCCESS");
        response.put("message", "회원가입 성공");
        response.put("data", userData);
        
        return response;
    }

    /** 로그인 */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody UserLoginRequestDTO dto) {
        System.out.println("[로그] 로그인 요청 DTO: " + dto);
        User user = userService.login(dto);
        String token = com.knittingapp.util.JwtUtil.generateToken(user.getEmail());
        
        // Map.of()는 null을 허용하지 않으므로 nickname이 null일 수 있는 경우 처리
        Map<String, Object> userData = new java.util.HashMap<>();
        userData.put("id", user.getId());
        userData.put("email", user.getEmail());
        userData.put("nickname", user.getNickname()); // null이어도 HashMap은 허용
        
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("status", 200);
        response.put("code", "LOGIN_SUCCESS");
        response.put("message", "로그인 성공");
        
        Map<String, Object> data = new java.util.HashMap<>();
        data.put("token", token);
        data.put("user", userData);
        response.put("data", data);
        
        return response;
    }
}
