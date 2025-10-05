package com.example.ecsite.repository;

import com.example.ecsite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// ユーザーデータの永続化と検索を行うリポジトリインターフェース
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}