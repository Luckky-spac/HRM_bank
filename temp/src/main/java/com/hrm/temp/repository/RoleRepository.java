package com.hrm.temp.repository;

import com.hrm.temp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    // ค้นหา Role ด้วยรหัส Role (role_id) 
    Optional<Role> findByRoleId(Long roleId); 
    
    // ค้นหา Role ด้วยชื่อ Role (role_name)
    Optional<Role> findByRoleName(String roleName); 
}