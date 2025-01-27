package com.spring.home_solver.DTO;

import com.spring.home_solver.DTO.userProfilAndAllPostDTO.UserProFileAndAllPostDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@AllArgsConstructor
public class UserProfileAndAllPostResponseDTO {
    private UserProFileAndAllPostDTO userProfile;
}
