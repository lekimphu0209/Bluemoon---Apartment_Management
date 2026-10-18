package com.qlcc.service;

import com.qlcc.entity.User;
import com.qlcc.exception.ApiException;
import com.qlcc.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Lay user dang dang nhap tu SecurityContext (JWT filter da set).
 * Dung cho cac endpoint can ghi createdBy / collectedBy.
 */
@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User requireCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw ApiException.unauthorized("Chưa đăng nhập.");
        }
        User user = userRepository.findByName(auth.getName());
        if (user == null) {
            throw ApiException.unauthorized("Không tìm thấy người dùng.");
        }
        return user;
    }
}
