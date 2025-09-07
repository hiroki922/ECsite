Spring Boot + Vue.js(TypeScript) で ECサイト（ログイン・商品一覧・カート機能）


全体構成
* バックエンド: Spring Boot (Java)
    * REST API を作成
    * ユーザー認証（JWT）
    * 商品一覧API
    * カートAPI
* フロントエンド: Vue 3 + TypeScript
    * Vue Router で画面遷移
    * Pinia(Vuexでも可) で状態管理
    * Axios で Spring Boot API と通信
* DB: MySQL / PostgreSQL（開発なら H2 でも可）

機能ごとの概要
1. 認証（ログイン）
* ユーザーは users テーブルに保存（id, email, passwordハッシュ）
* Spring Security + JWT でログイン認証
    * /auth/login に email, password をPOST
    * 正常なら JWT を返す
* フロント側は JWT を localStorage/sessionStorage に保存
* Axios の interceptor で毎回 Authorization ヘッダーに Bearer token を付与

2. 商品一覧
* products テーブル (id, name, description, price, imageUrl)
* Spring Boot 側で /products GET API を作成
* フロントで API から取得して一覧表示

3. カート機能
* 認証ユーザーごとにカートを保持
* DB設計例：
    * cart_items (id, user_id, product_id, quantity)
* API:
    * GET /cart → カート一覧
    * POST /cart → 商品を追加
    * PUT /cart/{itemId} → 数量変更
    * DELETE /cart/{itemId} → 削除
* フロントは Pinia でカートの状態を保持しつつ、更新時に API と同期

開発手順（ステップごと）
バックエンド（Spring Boot）
1. プロジェクト作成Spring Initializr選択:
    * Spring Boot 3.x
    * Dependencies: Spring Web, Spring Security, JPA, MySQL (or H2), Lombok
2. DB設定 (application.yml)spring:
3.   datasource:
4.     url: jdbc:mysql://localhost:3306/ecdb
5.     username: root
6.     password: password
7.   jpa:
8.     hibernate:
9.       ddl-auto: update
10.     show-sql: true
11. 
12. Entity作成
    * User, Product, CartItem を作成
13. 認証 (JWT) 実装
    * Spring Security 設定クラス
    * JWT Utils, Filter 実装
    * /auth/login API 実装
14. 商品API /products
    * GET で一覧を返す
15. カートAPI /cart
    * CRUD 実装

フロントエンド（Vue + TS）
1. プロジェクト作成npm init vue@latest ec-frontend
2. cd ec-frontend
3. npm install
4. npm install axios pinia vue-router
5. 
6. ディレクトリ構成src/
7.   api/        ← axios 設定
8.   store/      ← pinia
9.   views/      ← 各ページ
10.   components/ ← UIコンポーネント
11. 
12. ルーティング
    * /login ログインページ
    * /products 商品一覧
    * /cart カート
13. API通信 (axios)
    * interceptor で JWT を付与
    * api/auth.ts, api/products.ts, api/cart.ts
14. 状態管理 (Pinia)
    * useAuthStore (JWT, user)
    * useCartStore (カート内容)
15. UI
    * Vuetify / Element Plus などの UI ライブラリを使うと楽

ディレクトリ構造

ECsite/
├─ backend/                ← Spring Boot プロジェクト
│   ├─ src/
│   │   ├─ main/
│   │   │   ├─ java/com/example/ecsite/...
│   │   │   └─ resources/
│   │   │       ├─ application.yml
│   │   │       ├─ static/      ← 画像や静的ファイル
│   │   │       └─ templates/   ← Thymeleafを使う場合のみ
│   │   └─ test/java/...
│   ├─ pom.xml
│   └─ mvnw, mvnw.cmd
│
├─ frontend/               ← Vue 3 + TypeScript プロジェクト
│   ├─ src/
│   │   ├─ assets/
│   │   ├─ components/
│   │   ├─ views/
│   │   ├─ store/          ← Pinia
│   │   ├─ router/         ← Vue Router
│   │   └─ api/            ← axios 設定やAPI呼び出し
│   ├─ public/
│   ├─ package.json
│   └─ tsconfig.json
│
├─ .gitignore
├─ .gitattributes
└─ README.md