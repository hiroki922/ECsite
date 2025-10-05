package com.example.ecsite.model;

// JavaプロジェクトでJava Persistence API (JPA) の機能を使うために必要なパッケージをインポートするためのコード
// オブジェクトとデータベースのテーブルを関連付けることで、SQLを直接記述せずにデータ操作を行えるようにします。
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// ユーザーモデルを定義するクラス
@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;
}