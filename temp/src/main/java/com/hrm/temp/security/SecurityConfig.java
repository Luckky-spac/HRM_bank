// package com.hrm.temp.security; 

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// import java.util.Arrays;

// @Configuration 
// @EnableWebSecurity
// public class SecurityConfig {
    
//     // Bean นี้ยังคงอยู่เพื่อใช้งาน PasswordEncoder ใน Service Layer อื่นๆ
//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder(); 
//     }

//     // CORS Configuration Source 
//     @Bean
//     public CorsConfigurationSource corsConfigurationSource() {
//         CorsConfiguration configuration = new CorsConfiguration();
//         // แก้ไข: กำหนด Origin ที่ชัดเจนแทน "*", เนื่องจาก allowCredentials เป็น true
//         configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); 
        
//         configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//         configuration.setAllowedHeaders(Arrays.asList("*")); 
//         configuration.setAllowCredentials(true); 
        
//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         source.registerCorsConfiguration("/**", configuration);
//         return source;
//     }

//     // Security Filter Chain 
//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
//         http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

//         http.csrf(csrf -> csrf.disable());
        
//         http.authorizeHttpRequests(authorize -> authorize
//                 .anyRequest().permitAll() 
//         );

//         http.httpBasic(basic -> basic.disable()); 
//         http.formLogin(form -> form.disable());
        
//         return http.build();
//     }
// }