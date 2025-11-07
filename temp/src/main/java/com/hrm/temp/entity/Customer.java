package com.hrm.temp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CUS_ID")
    private String cusId;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "FIRSTNAME_LA")
    private String firstnameLa;

    @Column(name = "LASTNAME_LA")
    private String lastnameLa;

    @Column(name = "FIRSTNAME_EN")
    private String firstnameEn;

    @Column(name = "LASTNAME_EN")
    private String lastnameEn;

    // Getters
    public String getUserId() { return userId; }
    public String getFirstnameLa() { return firstnameLa; }
    public String getLastnameLa() { return lastnameLa; }
    public String getFirstnameEn() { return firstnameEn; }
    public String getLastnameEn() { return lastnameEn; }
}