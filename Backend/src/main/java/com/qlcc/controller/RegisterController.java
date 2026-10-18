package com.qlcc.controller;

import com.qlcc.dto.UserDTO;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class RegisterController {

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO request, 
                                          BindingResult result, 
                                          HttpSession session) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        // Lưu user vào session
        session.setAttribute("tempUser", request);
        session.setAttribute("email", request.getEmail());

        Map<String, String> response = new HashMap<>();
        response.put("message", "User saved to session successfully!");

        return ResponseEntity.ok(response);
    }
}