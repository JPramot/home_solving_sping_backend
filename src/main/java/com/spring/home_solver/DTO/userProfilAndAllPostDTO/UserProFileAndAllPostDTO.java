package com.spring.home_solver.DTO.userProfilAndAllPostDTO;

import com.spring.home_solver.DTO.postDTO.PostInfoDTO;
import com.spring.home_solver.enumulation.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserProFileAndAllPostDTO {

    private Integer id;

    private String firstName;

    private String lastName;

    private String alias;

    private LocalDate birthDate;

    private String introduction;

    private Gender gender;

    private String profileImage;

    private UserInfo user;

    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    public static class UserInfo {

        private String username;

        private Set<PostInfoDTO> posts;
    }
}
