package com.example.ecsite.model;

// JavaプロジェクトでJava Persistence API (JPA) の機能を使うために必要なパッケージをインポートするためのコード
// オブジェクトとデータベースのテーブルを関連付けることで、SQLを直接記述せずにデータ操作を行えるようにします。
import jakarta.persistence.*;


// ユーザーモデルを定義するクラス
@Entity
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    // GetterとSetterのメソッド
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
