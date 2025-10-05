package com.example.ecsite.exception;

// ユーザー登録時にメールアドレスが既に存在する場合に投げる例外クラス
// RuntimeException を継承することで、チェックされない例外(=throws宣言が不要)として扱える
public class DuplicateEmailException extends RuntimeException {

    // コンストラクタでエラーメッセージを受け取る
    public DuplicateEmailException(String message) {
        // 親クラス(RuntimeException)のコンストラクタに渡す
        super(message);
    }
}
