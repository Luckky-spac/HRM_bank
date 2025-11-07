// src/main/java/com/example/demo/controller/AuthController.java
package com.hrm.temp.controller;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hrm.temp.dto.LoginRequest;
import com.hrm.temp.dto.Message;
import com.hrm.temp.dto.RegisterRequest;
import com.hrm.temp.entity.Customer;
import com.hrm.temp.entity.User;
import com.hrm.temp.repository.CustomerRepository;
import com.hrm.temp.repository.UserRepository;
import com.hrm.temp.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired private AuthService authService;
    @Autowired private UserRepository userRepository;
    @Autowired private CustomerRepository customerRepository;

    // ---------------- REGISTER ----------------
    @PostMapping("/register")
    public ResponseEntity<Message> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request);
        Message msg = new Message();
        msg.setMessage("Register successful");
        msg.setUserId(user.getUserId());
        msg.setUsername(user.getUsername());
        return ResponseEntity.ok(msg);
    }

    // ---------------- LOGIN ----------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            String token = authService.login(request);
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // ✅ ดึง customer ด้วย CUS_ID จากตาราง users
            Optional<Customer> optCustomer = customerRepository.findByCusId(user.getCusId());
            Customer customer = optCustomer.orElse(null);

            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getUserId());
            userMap.put("username", user.getUsername());
            userMap.put("email", user.getEmail());
            userMap.put("role", "user");
            userMap.put("photo_url", user.getPhotoUrl() != null ? user.getPhotoUrl() : "");

            // ✅ เพิ่มชื่อจริงจาก customer
            if (customer != null) {
                userMap.put("nameLa", (customer.getFirstnameLa() + " " + customer.getLastnameLa()).trim());
                userMap.put("nameEn", (customer.getFirstnameEn() + " " + customer.getLastnameEn()).trim());
            } else {
                userMap.put("nameLa", "ไม่พบข้อมูล");
                userMap.put("nameEn", "ไม่พบข้อมูล");
            }

            return ResponseEntity.ok(Map.of("token", token, "user", userMap));

        } catch (Exception e) {
            System.err.println("LOGIN ERROR: " + e.getMessage());
            return ResponseEntity.status(401)
                    .body(new Message("Invalid email or password"));
        }
    }

    // ---------------- GET PROFILE ----------------
    @GetMapping("/user/profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ ดึง customer ด้วย CUS_ID
        Customer customer = customerRepository.findByCusId(user.getCusId()).orElse(null);

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getUserId());
        userMap.put("username", user.getUsername());
        userMap.put("email", user.getEmail());
        userMap.put("photo_url", user.getPhotoUrl() != null ? user.getPhotoUrl() : "");
        userMap.put("nameLa", customer != null ? 
            (customer.getFirstnameLa() + " " + customer.getLastnameLa()).trim() : "ไม่พบข้อมูล");
        userMap.put("nameEn", customer != null ? 
            (customer.getFirstnameEn() + " " + customer.getLastnameEn()).trim() : "ไม่พบข้อมูล");

        return ResponseEntity.ok(userMap);
    }

    // ---------------- UPDATE PROFILE ----------------
    @PostMapping(value = "/user/profile/update", consumes = "multipart/form-data")
    public ResponseEntity<?> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) MultipartFile photo
    ) {
        try {
            if (userDetails == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
            }

            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // ✅ เปลี่ยนชื่อ username
            if (name != null && !name.trim().isEmpty()) {
                user.setUsername(name.trim());
            }

            // ✅ อัปโหลดรูป
            if (photo != null && !photo.isEmpty()) {
                String dir = "uploads/profiles/";
                Files.createDirectories(Paths.get(dir));
                String fileName = "profile_" + user.getUserId() + "_" + System.currentTimeMillis() + ".jpg";
                Files.write(Paths.get(dir + fileName), photo.getBytes());
                user.setPhotoUrl("/" + dir + fileName);
            }

            userRepository.save(user);

            // ✅ ดึงชื่อจริงจาก customer โดยใช้ CUS_ID
            Customer customer = customerRepository.findByCusId(user.getCusId()).orElse(null);

            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getUserId());
            userMap.put("username", user.getUsername());
            userMap.put("email", user.getEmail());
            userMap.put("photo_url", user.getPhotoUrl() != null ? user.getPhotoUrl() : "");
            userMap.put("nameLa", customer != null ? 
                (customer.getFirstnameLa() + " " + customer.getLastnameLa()).trim() : "ไม่พบข้อมูล");
            userMap.put("nameEn", customer != null ? 
                (customer.getFirstnameEn() + " " + customer.getLastnameEn()).trim() : "ไม่พบข้อมูล");

            return ResponseEntity.ok(Map.of("user", userMap));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Update failed"));
        }
    }
}