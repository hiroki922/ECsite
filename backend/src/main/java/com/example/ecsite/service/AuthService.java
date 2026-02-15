package com.example.ecsite.service;

import com.example.ecsite.exception.DuplicateEmailException;
import com.example.ecsite.model.User;
import com.example.ecsite.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;
import com.example.ecsite.model.Role;

/*
 * 認証・ログイン関連（ユーザー登録やログイン処理など）
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * ユーザー登録を行うメソッド
     *
     * @param name     ユーザーの名前
     * @param email    ユーザーのメールアドレス
     * @param password ユーザーのパスワード
     * @return 登録されたユーザー
     */
    public User register(String name, String email, String password) {
        // 既に登録済みかチェック
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateEmailException("このメールアドレスは既に登録されています");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setRole(Role.USER); // ← デフォルトを必ずセット

        // パスワードをハッシュ化して保存
        String hashedPassword = passwordEncoder.encode(password);
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    /**
     * ユーザーのログインを処理するメソッド
     *
     * @param email    ユーザーのメールアドレス
     * @param password ユーザーのパスワード
     * @return ログイン成功時はUser、失敗時はfalse
     */
    public Optional<User> login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}