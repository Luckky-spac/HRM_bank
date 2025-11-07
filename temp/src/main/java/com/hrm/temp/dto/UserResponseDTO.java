package com.hrm.temp.dto;

import java.time.LocalDateTime;

public class UserResponseDTO {
    
    private Long id;
    private String user_id;
    private String username;
    private String email;
    private String status; 

    private String cus_id; 
    private String emp_id; 

    private Long role_id; 
    private String role_name; 

    private LocalDateTime create_at; 
    private LocalDateTime update_at; 
    
    // Constructors
    public UserResponseDTO() {}
    public UserResponseDTO(Long id, String user_id, String username, String email, String status, String cus_id, String emp_id, Long role_id, String role_name, LocalDateTime create_at, LocalDateTime update_at) {
        this.id = id;
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.status = status;
        this.cus_id = cus_id;
        this.emp_id = emp_id;
        this.role_id = role_id;
        this.role_name = role_name;
        this.create_at = create_at;
        this.update_at = update_at;
    }

    // Getters & Setters
    public Long get_id() { return id; }
    public void set_id(Long id) { this.id = id; }

    public String get_user_id() { return user_id; }
    public void set_user_id(String user_id) { this.user_id = user_id; }

    public String get_username() { return username; }
    public void set_username(String username) { this.username = username; }

    public String get_email() { return email; }
    public void set_email(String email) { this.email = email; }

    public String get_status() { return status; }
    public void set_status(String status) { this.status = status; }

    public String get_cus_id() { return cus_id; }
    public void set_cus_id(String cus_id) { this.cus_id = cus_id; }

    public String get_emp_id() { return emp_id; }
    public void set_emp_id(String emp_id) { this.emp_id = emp_id; }

    public Long get_role_id() { return role_id; }
    public void set_role_id(Long role_id) { this.role_id = role_id; }

    public String get_role_name() { return role_name; }
    public void set_role_name(String role_name) { this.role_name = role_name; }

    public LocalDateTime get_create_at() { return create_at; }
    public void set_create_at(LocalDateTime create_at) { this.create_at = create_at; }

    public LocalDateTime get_update_at() { return update_at; }
    public void set_update_at(LocalDateTime update_at) { this.update_at = update_at; }
}