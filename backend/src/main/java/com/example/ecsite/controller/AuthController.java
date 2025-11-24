package com.example.ecsite.controller;

// User クラスを使うため。ユーザ情報（名前・メール・パスワードなど）を扱う
import com.example.ecsite.dto.UserDto;
import com.example.ecsite.model.User;
// REST コントローラを作成するためのアノテーション @RestController を使う
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
// HTTP POST リクエストを処理するためのアノテーション @PostMapping を使う
import org.springframework.web.bind.annotation.PostMapping;
// リクエストボディの JSON をオブジェクトに変換する @RequestBody を使う
import org.springframework.web.bind.annotation.RequestBody;
// クラス全体の URL パスを指定する @RequestMapping を使う
import org.springframework.web.bind.annotation.RequestMapping;
// ビジネスロジックを担当する AuthService を使うためのインポート
import com.example.ecsite.service.AuthService;
// HTTPレスポンスを返すために使用するクラス
import org.springframework.http.ResponseEntity;
// Map を使ってレスポンスボディを作成するためのインポート
import java.util.Map;
// ログインリクエストのデータを受け取るためのクラス
import com.example.ecsite.dto.LoginRequest;

import com.example.ecsite.dto.LoginResponse;
// HTTP ステータスコードを扱うためのインポート
import org.springframework.http.HttpStatus;

/**
 * 認証関連のコントローラー
 * このクラスでは、ユーザー登録やログインなど
 * 認証に関わる APIとする
 */
// 認証関連のAPIエンドポイントを提供するコントローラー
@SuppressWarnings("unused")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * ユーザー登録エンドポイント
     * URL: POST /register
     * リクエスト: JSON 形式でユーザー情報（name, email, password）
     * レスポンス: 登録された User オブジェクト
     *
     * @param user フロントから送られてくるユーザー情報
     * @return 登録されたユーザー情報
     */
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        // リクエストボディからユーザー情報を取得
        return authService.register(user.getName(), user.getEmail(), user.getPassword());
    }

    /**
     * ユーザーログインエンドポイント
     * URL: POST /login
     * リクエスト: JSON 形式で LoginRequest（email, password）
     * レスポンス:
     *   - 成功: HTTP 200 と LoginResponse（isLoggedIn: true, user: UserDto）
     *   - 失敗: HTTP 401 とエラーメッセージ
     *
     * @param request フロントから送られてくるログイン情報
     * @return ResponseEntity<LoginResponse> または エラーメッセージ
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {

        var user = authService.login(request.getEmail(), request.getPassword());

        if (user.isPresent()) {
            User u = user.get();
            // ログイン成功時にセッションを作成して JSESSIONID を発行
            httpRequest.getSession(true);

            var userDto = new UserDto(u.getName());
            var response = new LoginResponse(true, "ログイン成功", userDto, u.getRole().name());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "メールアドレスまたはパスワードが間違っています"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // セッションを無効化
        var session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // クッキーも削除
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // 有効期限を0にして削除
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("message", "ログアウトしました"));
    }
}    