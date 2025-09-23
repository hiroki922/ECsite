 ##ドキュメント構造

backend/
├─ pom.xml                        # Maven 設定ファイル
├─ src/
│  ├─ main/
│  │  ├─ java/
│  │  │  └─ com/
│  │  │     └─ example/
│  │  │        └─ ecsite/
│  │  │           ├─ EcsiteApplication.java     # Spring Boot メイン
│  │  │           ├─ controller/                # コントローラー層
│  │  │           │  └─ AuthController.java
│  │  │           ├─ service/                   # ビジネスロジック層
│  │  │           │  └─ AuthService.java
│  │  │           ├─ repository/                # DB アクセス層
│  │  │           │  └─ UserRepository.java
│  │  │           ├─ model/                     # エンティティ層
│  │  │           │   └─ User.java
│  │  │           └─ config/
│  │  │               └─ SecurityConfig.java     ← Spring Security 設定クラス
│  │  │ 
│  │  └─ resources/
│  │     ├─ application.properties              # 設定ファイル
│  │     └─ data.sql                            # 初期データ（必要なら）
│  └─ test/
│     └─ java/
│        └─ com/example/ecsite/                 # テストコード用
│           └─ EcsiteApplicationTests.java
