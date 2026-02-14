package com.example.ecsite.controller;

import com.example.ecsite.dto.AdminUserResponse;
import com.example.ecsite.dto.RoleUpdateRequest;
import com.example.ecsite.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public List<AdminUserResponse> listUsers() {
        return adminUserService.listUsers();
    }

    @PutMapping("/{id}/role")
    public AdminUserResponse updateRole(@PathVariable Long id, @RequestBody RoleUpdateRequest request) {
        return adminUserService.updateRole(id, request);
    }
}
