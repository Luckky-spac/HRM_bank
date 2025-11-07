package com.hrm.temp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ROLE_ID", unique = true, nullable = false)
    private Long roleId;  

    @Column(name = "ROLE_NAME", unique = true, nullable = false)
    private String roleName; 

    @Column(name = "DESCRIPTION")
    private String description; 

    public Role() {}

    public Role(Long id, Long roleId, String roleName, String description) {
        this.id = id;
        this.roleId = roleId;
        this.roleName = roleName;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
