package com.spring.home_solver.controller;

import com.spring.home_solver.DTO.*;
import com.spring.home_solver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/ban")
    public ResponseEntity<AllBannedUserResponseDTO> getAllBannedUser() {
        AllBannedUserResponseDTO responseDTO = userService.getAllBannedUser();
        return ResponseEntity.ok(responseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{userId}/ban")
    public ResponseEntity<ApiMessageResponse> banUser(@PathVariable Integer userId) {
        ApiMessageResponse response = userService.banUser(userId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{userId}/unban")
    public ResponseEntity<ApiMessageResponse> unbanUser(@PathVariable Integer userId) {
        ApiMessageResponse response = userService.unBanUser(userId);
        return ResponseEntity.ok(response);
    }
}
