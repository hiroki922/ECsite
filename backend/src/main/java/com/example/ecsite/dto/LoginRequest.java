package com.example.ecsite.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


/**
 * ログインリクエストの DTO
 * フロントから送られてくるログイン情報を受け取る
 */
@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}