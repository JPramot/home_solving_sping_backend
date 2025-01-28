package com.spring.home_solver.DTO;

import com.spring.home_solver.enumulation.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannedUserDTO {

    private Integer id;

    private String username;

    private String email;

    private String createdAt;

    private Boolean isBan;

    private Role role;
}
