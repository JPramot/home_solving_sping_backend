package com.spring.home_solver.service;

import com.spring.home_solver.DTO.UpdateUserProfileDTO;
import com.spring.home_solver.DTO.UserProfileAndAllPostResponseDTO;
import com.spring.home_solver.DTO.UserProfileResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    UserProfileResponseDTO createOrUpdateProfile(UpdateUserProfileDTO updateUserProfileDTO,
                                                 MultipartFile image);

    UserProfileAndAllPostResponseDTO getUserProfileByUserId(Integer userId);
}
