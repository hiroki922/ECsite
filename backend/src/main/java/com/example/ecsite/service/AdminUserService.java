package com.example.ecsite.service;

import com.example.ecsite.dto.AdminUserResponse;
import com.example.ecsite.dto.RoleUpdateRequest;
import com.example.ecsite.model.Role;
import com.example.ecsite.model.User;
import com.example.ecsite.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<AdminUserResponse> listUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AdminUserResponse updateRole(Long id, RoleUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));
        user.setRole(Role.valueOf(request.getRole()));
        return toResponse(userRepository.save(user));
    }

    private AdminUserResponse toResponse(User user) {
        AdminUserResponse r = new AdminUserResponse();
        r.setId(user.getId());
        r.setName(user.getName());
        r.setEmail(user.getEmail());
        r.setRole(user.getRole().name());
        return r;
    }
}
