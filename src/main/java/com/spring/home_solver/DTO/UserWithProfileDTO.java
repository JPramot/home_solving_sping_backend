package com.spring.home_solver.DTO;

import com.spring.home_solver.enumulation.Gender;
import com.spring.home_solver.enumulation.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UserWithProfileDTO {

    private Integer id;

    private String username;

    private String email;

    private Boolean isBan;

    private Boolean isDelete;

    private LocalDateTime createdAt;

    private UserProfileInfo userProfile;

    private Role role;

    @Data
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class UserProfileInfo {

        private Integer id;

        private String firstName;

        private String lastName;

        private String alias;

        private LocalDate birthDate;

        private String introduction;

        private Gender gender;

        private String profileImage;

        private Integer userId;
    }
}
