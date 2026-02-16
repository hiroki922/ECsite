# ECサイト

Spring Boot + Vue 3 (TypeScript) で構築したフルスタック EC サイトです。

## 技術スタック

| レイヤー | 技術 |
|---------|------|
| バックエンド | Java 17 / Spring Boot 3.5.5 / Spring Security 6 |
| フロントエンド | Vue 3.5 / TypeScript / Vite 7 / Tailwind CSS 4 |
| 状態管理 | Pinia 3 |
| HTTP通信 | Axios（Cookie ベースセッション認証） |
| データベース | MySQL 8.0 |
| ORM | Spring Data JPA / Hibernate |

## 主な機能

### ユーザー向け
- ユーザー登録・ログイン（セッション認証）
- 商品一覧・商品詳細
- カート（追加・数量変更・削除）
- 配送先管理（追加・編集・削除・デフォルト設定）
- チェックアウト（配送先選択 → 注文確定 → 在庫減算）
- 注文履歴・注文詳細

### 管理者向け
- ダッシュボード（売上・注文数・ユーザー数の統計）
- 商品管理（CRUD）
- 注文管理（ステータス変更）
- ユーザー管理（ロール変更）

## ディレクトリ構成

```
ECsite/
├─ backend/                             # Spring Boot
│  ├─ src/main/java/com/example/ecsite/
│  │  ├─ config/                        # SecurityConfig 等
│  │  ├─ controller/                    # REST API エンドポイント
│  │  ├─ service/                       # ビジネスロジック
│  │  ├─ repository/                    # データアクセス (JPA)
│  │  ├─ model/                         # エンティティ
│  │  ├─ dto/                           # リクエスト/レスポンス DTO
│  │  └─ exception/                     # カスタム例外
│  ├─ src/main/resources/
│  │  ├─ application.properties         # DB接続・JPA設定
│  │  └─ data.sql                       # 初期データ投入用
│  └─ pom.xml
│
├─ frontend/                            # Vue 3 + TypeScript
│  ├─ src/
│  │  ├─ api/                           # API クライアント (Axios)
│  │  ├─ components/                    # 共通コンポーネント
│  │  ├─ views/                         # ページコンポーネント
│  │  │  └─ admin/                      # 管理画面
│  │  ├─ stores/                        # Pinia ストア (auth, cart)
│  │  ├─ router/                        # Vue Router
│  │  ├─ lib/                           # ユーティリティ
│  │  └─ assets/                        # CSS
│  ├─ package.json
│  └─ vite.config.ts
│
└─ README.md
```

## 環境構築

### 前提条件

- **Java** 17 以上
- **Maven** 3.8 以上（または同梱の `mvnw` を使用）
- **Node.js** 20.19 以上 または 22.12 以上
- **npm** 10 以上
- **MySQL** 8.0 以上

### 1. MySQL のセットアップ

MySQL をインストールし、データベースを作成します。

```bash
mysql -u root -p
```

```sql
CREATE DATABASE ecsite DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

> デフォルトの接続情報は `root` / `password` です。
> 変更する場合は `backend/src/main/resources/application.properties` を編集してください。

### 2. バックエンドの起動

```bash
cd backend

# 依存関係のダウンロード & ビルド
./mvnw clean install -DskipTests

# 起動（デフォルト: http://localhost:8080）
./mvnw spring-boot:run
```

初回起動時に Hibernate がテーブルを自動作成します（`ddl-auto=update`）。

#### 初期データの投入（任意）

初期データを投入したい場合は `application.properties` を編集します。

```properties
spring.sql.init.mode=always
```

起動後にデータが投入されたら、`never` に戻してください。

### 3. フロントエンドの起動

```bash
cd frontend

# 依存関係のインストール
npm install

# 開発サーバー起動（デフォルト: http://localhost:5173）
npm run dev
```

### 4. 動作確認

1. http://localhost:5173 にアクセス
2. 「新規登録」からユーザーを作成
3. ログインして商品一覧・カート・注文を確認

### 管理者アカウント

管理画面（`/admin`）にアクセスするには `ADMIN` ロールが必要です。
初期データまたは DB を直接編集してユーザーの `role` カラムを `ADMIN` に設定してください。

```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'your-email@example.com';
```

## API エンドポイント一覧

### 認証
| メソッド | パス | 説明 |
|---------|------|------|
| POST | `/api/auth/register` | ユーザー登録 |
| POST | `/api/auth/login` | ログイン |
| POST | `/api/auth/logout` | ログアウト |
| GET | `/api/auth/me` | ログインユーザー情報取得 |

### 商品
| メソッド | パス | 説明 |
|---------|------|------|
| GET | `/api/products` | 商品一覧 |
| GET | `/api/products/{id}` | 商品詳細 |

### カート
| メソッド | パス | 説明 |
|---------|------|------|
| GET | `/api/cart` | カート取得 |
| POST | `/api/cart` | 商品追加 |
| PUT | `/api/cart/{id}` | 数量変更 |
| DELETE | `/api/cart/{id}` | 商品削除 |

### 配送先
| メソッド | パス | 説明 |
|---------|------|------|
| GET | `/api/addresses` | 一覧取得 |
| POST | `/api/addresses` | 新規追加 |
| PUT | `/api/addresses/{id}` | 編集 |
| DELETE | `/api/addresses/{id}` | 削除 |
| POST | `/api/addresses/{id}/default` | デフォルト設定 |

### 注文
| メソッド | パス | 説明 |
|---------|------|------|
| POST | `/api/orders` | 注文作成 |
| GET | `/api/orders` | 注文履歴 |
| GET | `/api/orders/{id}` | 注文詳細 |

### 管理者
| メソッド | パス | 説明 |
|---------|------|------|
| POST | `/api/admin/products` | 商品登録 |
| PUT | `/api/admin/products/{id}` | 商品更新 |
| DELETE | `/api/admin/products/{id}` | 商品削除 |
| GET | `/api/admin/users` | ユーザー一覧 |
| PUT | `/api/admin/users/{id}/role` | ロール変更 |
| GET | `/api/admin/orders` | 全注文一覧 |
| PUT | `/api/admin/orders/{id}/status` | 注文ステータス変更 |
| GET | `/api/admin/stats` | ダッシュボード統計 |
