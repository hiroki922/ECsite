package com.example.ecsite.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private boolean loggedIn;  // ログイン成功かどうか
    private String message;    // 成功・失敗メッセージ）
    private UserDto user;      // ログインユーザー情報（成功時のみ）
    private String role;       // 権限ロール
}
