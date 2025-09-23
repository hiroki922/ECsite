package com.example.ecsite.config;

// Spring の設定クラスであることを示す
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

// Spring Security を有効化するためのアノテーション
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

// Spring Security のフィルタチェーンを作成するクラス
import org.springframework.security.web.SecurityFilterChain;

// Spring MVC の CORS 設定で使う
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// パラメータが null でないことを明示するアノテーション
import org.springframework.lang.NonNull;

// HttpSecurity を使ってセキュリティ設定を行う
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // セキュリティフィルタチェーンの設定
    @Bean //Bean：Spring が管理するオブジェクトを生成するメソッドに付与するアノテーション
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> {}) // CORS設定を有効化
            .csrf(csrf -> csrf.disable()) // SPAではCSRF無効化も検討
            .authorizeHttpRequests(auth ->auth
                .requestMatchers("/api/register").permitAll() // 登録エンドポイントは全員アクセス可能
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // OPTIONS は全て許可
                .anyRequest().authenticated() // その他は認証が必要
            )
            .formLogin(form -> form.disable()); // フォームログイン無効化

        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173") // フロントエンドのURLに合わせて変更
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}

