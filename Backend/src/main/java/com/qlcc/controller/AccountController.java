package com.qlcc.controller;

import com.qlcc.dto.SpecDtos.MeRequest;
import com.qlcc.dto.SpecDtos.PasswordRequest;
import com.qlcc.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** Tai khoan (F-TA-01..03). Duoi /api/auth/** da permitAll nen check auth trong service. */
@RestController
@RequestMapping("/api/auth")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me() {
        return ResponseEntity.ok(accountService.me());
    }

    @PutMapping("/me")
    public ResponseEntity<Map<String, Object>> updateMe(@Valid @RequestBody MeRequest req) {
        return ResponseEntity.ok(accountService.updateMe(req));
    }

    @PutMapping("/password")
    public ResponseEntity<Map<String, String>> changePassword(@Valid @RequestBody PasswordRequest req) {
        accountService.changePassword(req);
        return ResponseEntity.ok(Map.of("message", "Đã đổi mật khẩu."));
    }
}
