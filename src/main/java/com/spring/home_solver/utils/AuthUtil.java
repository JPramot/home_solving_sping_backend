package com.spring.home_solver.utils;

import com.spring.home_solver.entity.User;
import com.spring.home_solver.exception.NotFoundExc;
import com.spring.home_solver.repository.UserRepository;
import com.spring.home_solver.security.service.UserDetailImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    private UserRepository userRepository;

    public User loginUser() {
        Authentication auth = getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(()->new NotFoundExc("user", "username", username));
    }

    public Integer loginUserId() {
        Authentication auth = getAuthentication();
        UserDetailImpl user = (UserDetailImpl) auth.getPrincipal();
        return user.getId();
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
