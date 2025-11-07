package com.hrm.temp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hrm.temp.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 🔹 ใช้ตอน Login / Auth
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    // 🔹 ใช้เชื่อมกับตาราง Customer ด้วย CUS_ID
    Optional<User> findByCusId(String cusId);
}