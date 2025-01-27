package com.spring.home_solver.DTO;

import com.spring.home_solver.enumulation.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserProfileDTO {

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String alias;

    private String introduction;

    private Gender gender;

}
