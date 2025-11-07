package com.hrm.temp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hrm.temp.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // ใช้ค้นหา customer ด้วย CUS_ID แทน USER_ID
    Optional<Customer> findByCusId(String cusId);
}