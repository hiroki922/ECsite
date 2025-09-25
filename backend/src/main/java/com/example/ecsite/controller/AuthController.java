package com.example.ecsite.controller;

// User クラスを使うため。ユーザ情報（名前・メール・パスワードなど）を扱う
import com.example.ecsite.model.User;

// REST コントローラを作成するためのアノテーション @RestController を使う
import org.springframework.web.bind.annotation.RestController;

// HTTP POST リクエストを処理するためのアノテーション @PostMapping を使う
import org.springframework.web.bind.annotation.PostMapping;

// リクエストボディの JSON をオブジェクトに変換する @RequestBody を使う
import org.springframework.web.bind.annotation.RequestBody;

// Spring が AuthService を自動で DI（依存注入）するための @Autowired を使う
import org.springframework.beans.factory.annotation.Autowired;

// クラス全体の URL パスを指定する @RequestMapping を使う
import org.springframework.web.bind.annotation.RequestMapping;

// ビジネスロジックを担当する AuthService を使うためのインポート
import com.example.ecsite.service.AuthService;

// 認証関連のAPIエンドポイントを提供するコントローラー
@RestController
@RequestMapping("/api")
public class AuthController {

    // 認証サービス
    @Autowired
    private AuthService authService;

    // ユーザー登録エンドポイント
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        // リクエストボディからユーザー情報を取得
        return authService.register(user.getName(), user.getEmail(), user.getPassword());
    }    
}