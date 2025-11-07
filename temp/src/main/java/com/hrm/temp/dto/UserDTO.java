package com.hrm.temp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserDTO {

    @NotBlank(message = "Username ຕ້ອງບໍ່ວ່າງ")
    private String username;

    @NotBlank(message = "Email ຕ້ອງບໍ່ວ່າງ")
    @Email(message = "ຮູບແບບ Email ບໍ່ຖືກຕ້ອງ")
    private String email;

    private String password; 
    
    @NotNull(message = "ຕ້ອງເລືອກ Role")
    private Long role_id;
    
    private String cus_id; 
    private String emp_id; 
    
    private String status; 

    // Constructors 
    public UserDTO() {}
    public UserDTO(String username, String email, String password, Long role_id, String cus_id, String emp_id, String status) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role_id = role_id;
        this.cus_id = cus_id;
        this.emp_id = emp_id;
        this.status = status;
    }

    // Getters & Setters
    public String get_username() { return username; }
    public void set_username(String username) { this.username = username; }

    public String get_email() { return email; }
    public void set_email(String email) { this.email = email; }

    public String get_password() { return password; }
    public void set_password(String password) { this.password = password; }

    public Long get_role_id() { return role_id; }
    public void set_role_id(Long role_id) { this.role_id = role_id; }

    public String get_cus_id() { return cus_id; }
    public void set_cus_id(String cus_id) { this.cus_id = cus_id; }

    public String get_emp_id() { return emp_id; }
    public void set_emp_id(String emp_id) { this.emp_id = emp_id; }

    public String get_status() { return status; }
    public void set_status(String status) { this.status = status; }
}