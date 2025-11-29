package com.knittingapp.service;

import com.knittingapp.domain.User;
import com.knittingapp.dto.UserRegisterRequestDTO;
import com.knittingapp.dto.UserLoginRequestDTO;
import com.knittingapp.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * 회원(User) 서비스
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입
    public User register(UserRegisterRequestDTO dto) {
        String email = dto.email().trim();
        String password = dto.password().trim();
        
        System.out.println("[회원가입 시작] 이메일: " + email);
        
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("비밀번호는 6자 이상이어야 합니다.");
        }
        
        User user = new User();
        user.setEmail(email);
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        
        System.out.println("[회원가입] 원본 비밀번호: " + password);
        System.out.println("[회원가입] 암호화된 비밀번호: " + encodedPassword);
        
        User savedUser = userRepository.save(user);
        System.out.println("[회원가입 성공] 사용자: " + savedUser.getEmail());
        
        return savedUser;
    }

    // 로그인 (비밀번호 검증)
    public User login(UserLoginRequestDTO dto) {
        String email = dto.email().trim();
        String password = dto.password().trim();
        
        System.out.println("[로그인 시작] 이메일: " + email);
        
        // 이메일로 사용자 조회
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            System.out.println("[로그인 실패] 사용자를 찾을 수 없음: " + email);
            throw new IllegalArgumentException("회원정보가 없습니다. 회원가입 먼저 해주세요.");
        }
        
        User user = userOpt.get();
        System.out.println("[로그인 검증] 사용자 찾음: " + user.getEmail());
        System.out.println("[로그인 검증] 입력 비밀번호: " + password);
        System.out.println("[로그인 검증] DB 비밀번호(암호화): " + user.getPassword());
        
        // 비밀번호 검증
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        System.out.println("[로그인 검증] 비밀번호 일치 여부: " + matches);
        
        if (!matches) {
            System.out.println("[로그인 실패] 비밀번호 불일치");
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        
        System.out.println("[로그인 성공] 사용자: " + user.getEmail());
        return user;
    }

    public void testPasswordMatch() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String raw = "123456";
        String encoded = "$2a$10$7ZEmRcSqvL7Xwe5v6Kl2GOSSCaWfgyTizBNJWwmijp9DItkmNOFLq";
        System.out.println("테스트: " + encoder.matches(raw, encoded)); // true면 정상
    }
}
