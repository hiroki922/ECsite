package com.example.ecsite.service;

import com.example.ecsite.model.User;
import com.example.ecsite.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String name, String email, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);

        // パスワードをハッシュ化して保存
        String hashedPassword = passwordEncoder.encode(password);
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    // public boolean login(String email, String rawPassword) {
    //     User user = userRepository.findByEmail(email);
    //     if (user == null) {
    //         return false;
    //     }
    //     return passwordEncoder.matches(rawPassword, user.getPassword());
    // }
}