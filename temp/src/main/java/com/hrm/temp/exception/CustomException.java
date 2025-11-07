package com.hrm.temp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// @ResponseStatus บอก Spring ว่าเมื่อเกิด Exception นี้ ให้ตอบกลับด้วย HTTP 400
@ResponseStatus(HttpStatus.BAD_REQUEST) 
public class CustomException extends RuntimeException {

    // Constructor ที่รับ String (แก้ไขปัญหาข้อ 2)
    public CustomException(String message) {
        super(message);
    }

    // Constructor เสริม (ถ้าต้องการใส่ Exception ต้นทาง)
    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }
}