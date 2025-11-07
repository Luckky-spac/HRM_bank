package com.hrm.temp.controller;

import com.hrm.temp.entity.Role;
import com.hrm.temp.repository.RoleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // GET /api/roles
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        // ดึง Entity โดยตรง
        List<Role> roles = roleRepository.findAll();
        // ใน Production ควรแปลงเป็น DTO ก่อนส่งกลับ
        return ResponseEntity.ok(roles);
    }
}