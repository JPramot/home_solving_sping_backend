package com.spring.home_solver.service;

import com.spring.home_solver.DTO.*;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    UserProfileResponseDTO createOrUpdateProfile(UpdateUserProfileDTO updateUserProfileDTO,
                                                 MultipartFile image);

    UserProfileAndAllPostResponseDTO getUserProfileByUserId(Integer userId);

    ApiMessageResponse banUser(Integer userId);

    ApiMessageResponse unBanUser(Integer userId);

    AllBannedUserResponseDTO getAllBannedUser();
}
