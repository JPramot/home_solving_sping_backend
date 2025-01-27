package com.spring.home_solver.DTO;

import com.spring.home_solver.DTO.postDTO.PostInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AllPostResponseDTO {

    private List<PostInfoDTO> posts;
}
