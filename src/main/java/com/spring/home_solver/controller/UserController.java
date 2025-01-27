package com.spring.home_solver.controller;

import com.spring.home_solver.DTO.UpdateUserProfileDTO;
import com.spring.home_solver.DTO.UserProfileAndAllPostResponseDTO;
import com.spring.home_solver.DTO.UserProfileResponseDTO;
import com.spring.home_solver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PatchMapping
    public ResponseEntity<UserProfileResponseDTO> createOrUpdateProfile(@ModelAttribute UpdateUserProfileDTO body,
                                                                      @RequestPart(value = "profileImage", required = false)
                                                                      MultipartFile image) {
        UserProfileResponseDTO responseDTO = userService.createOrUpdateProfile(body,image);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserProfileAndAllPostResponseDTO> getProfileByUserId(@PathVariable Integer userId) {
        UserProfileAndAllPostResponseDTO responseDTO = userService.getUserProfileByUserId(userId);
        return ResponseEntity.ok(responseDTO);
    }
}
