package com.example.ecsite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

// グローバル例外ハンドラークラス
// コントローラーで発生した例外をキャッチして適切なHTTPレスポンスを返す
@RestControllerAdvice
public class GlobalExceptionHandler {

    // DuplicateEmailExceptionが発生した場合に呼び出されるメソッド
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateEmail(DuplicateEmailException ex) {
        // エラーレスポンスを作成
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400を返す
    }
}
