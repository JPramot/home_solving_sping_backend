package com.spring.home_solver.DTO.postDTO;

import com.spring.home_solver.enumulation.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserInPost {

        private Role role;

        private Integer id;

        private ProfileInUser userProfile;

}
