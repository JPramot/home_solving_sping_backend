package com.spring.home_solver.controller;

import com.spring.home_solver.DTO.*;
import com.spring.home_solver.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserInfoResponseDTO> registerUser(@RequestBody @Valid UserRegisterDTO body) {

        UserInfoResponseDTO responseDTO = authService.register(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<UserInfoResponseDTO> login(@RequestBody @Valid UserLoginDTO body) {
        UserInfoResponseDTO responseDTO = authService.login(body);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/me")
    public ResponseEntity<UserWithProfileResponseDTO> getMe() {
        UserWithProfileResponseDTO responseDTO = authService.getMe();
        return ResponseEntity.ok(responseDTO);
    }
}
