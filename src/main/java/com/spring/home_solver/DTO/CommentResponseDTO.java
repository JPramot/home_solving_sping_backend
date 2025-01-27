package com.spring.home_solver.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {

    private Integer id;

    private String content;

    private LocalDateTime createdAt;

    private Boolean isDelete;

    private Integer userId;

    private Integer postId;

    private UserWhoComment user;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class UserWhoComment {
        private Integer id;
    }
}
