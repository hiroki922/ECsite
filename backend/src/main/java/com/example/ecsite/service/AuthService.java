package com.example.ecsite.service;

import com.example.ecsite.exception.DuplicateEmailException;
import com.example.ecsite.model.User;
import com.example.ecsite.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;

/*
 * 認証・ログイン関連（ユーザー登録やログイン処理など）
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
        }

    /**
     * ユーザー登録を行うメソッド
     * @param name ユーザーの名前
     * @param email ユーザーのメールアドレス
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

        // パスワードをハッシュ化して保存
        String hashedPassword = passwordEncoder.encode(password);
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    /**
     * ユーザーのログインを処理するメソッド
     * @param email ユーザーのメールアドレス
     * @param password ユーザーのパスワード
     * @return ログイン成功時はtrue、失敗時はfalse
     */
    public boolean login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }

    /**
     * メールアドレスが既に登録されているか確認するメソッド
     * @param email ユーザーのメールアドレス
     * @return 登録されている場合はtrue、そうでない場合はfalse
     */
    public boolean isEmailRegistered(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}