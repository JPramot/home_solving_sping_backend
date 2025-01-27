package com.spring.home_solver.DTO.postDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PostByIdResponseDTO {

    private Integer id;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private Boolean canComment;

    private Boolean isDelete;

    private List<CommentDTO> comments;

    private List<PostImageDTO> postImages;

    private UserWhoPost user;

    private Integer userId;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class PostImageDTO {
        private Integer id;

        private String image;

        private Integer postId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class UserWhoPost {
        private String username;

        private ProfileInUser userProfile;
    }

}
