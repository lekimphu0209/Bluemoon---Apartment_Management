package com.qlcc.service;

import com.qlcc.dto.SpecDtos.MeRequest;
import com.qlcc.dto.SpecDtos.PasswordRequest;
import com.qlcc.entity.User;
import com.qlcc.exception.ApiException;
import com.qlcc.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/** Tai khoan (F-TA-01..03): xem/sua ho ten, doi mat khau. */
@Service
public class AccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUserService currentUserService;

    public AccountService(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          CurrentUserService currentUserService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.currentUserService = currentUserService;
    }

    public Map<String, Object> me() {
        User u = currentUserService.requireCurrentUser();
        Map<String, Object> d = new LinkedHashMap<>();
        d.put("id", u.getId());
        d.put("username", u.getName());
        d.put("fullName", u.getFullName());
        d.put("email", u.getEmail());
        d.put("role", u.getRole());
        d.put("residentId", u.getResidentId());
        return d;
    }

    public Map<String, Object> updateMe(MeRequest req) {
        User u = currentUserService.requireCurrentUser();
        u.setFullName(req.getFullName());
        userRepository.save(u);
        return me();
    }

    public void changePassword(PasswordRequest req) {
        User u = currentUserService.requireCurrentUser();
        String stored = u.getPasswordHash() != null ? u.getPasswordHash() : u.getPassword();
        if (!passwordEncoder.matches(req.getOldPassword(), stored)) {
            throw ApiException.fieldError("oldPassword", "Mật khẩu hiện tại không đúng.");
        }
        String encoded = passwordEncoder.encode(req.getNewPassword());
        u.setPassword(encoded); // ghi ca password + password_hash
        userRepository.save(u);
    }
}
