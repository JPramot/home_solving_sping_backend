package com.spring.home_solver.DTO;

import com.spring.home_solver.enumulation.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class UserInfoDTO {

    private Integer id;

    private String username;

    private String email;

    private Role role;

    private LocalDateTime createdAt;

    private Boolean isBan;
}
