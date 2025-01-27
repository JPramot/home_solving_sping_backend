package com.spring.home_solver.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PostResponseDTO {

    private Integer id;

    private String title;

    private String content;

    private Boolean isDelete;

    private Boolean canComment;

    private Set<PostImageInfo> postImages;
}
