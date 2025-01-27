package com.spring.home_solver.service;

import com.spring.home_solver.DTO.*;

public interface AuthService {

    UserInfoResponseDTO register(UserRegisterDTO body);

    UserInfoResponseDTO login(UserLoginDTO body);

    UserWithProfileResponseDTO getMe();
}
