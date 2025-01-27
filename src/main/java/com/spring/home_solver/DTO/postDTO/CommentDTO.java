package com.spring.home_solver.DTO.postDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CommentDTO {

    private Integer id;

    private LocalDateTime createdAt;

    private String content;

    private Integer postId;

    private UserWhoComment user;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class UserWhoComment {
        private Integer id;

        private ProfileInUser userProfile;
    }
}
