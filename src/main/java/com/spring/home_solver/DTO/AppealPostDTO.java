package com.spring.home_solver.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AppealPostDTO {

    private Integer id;

    private String content;

    private LocalDateTime createdAt;

    private Integer userId;

    private Integer postId;

    private PostAppealedInfo posts;

    private AppealPostOwner user;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class PostAppealedInfo {
        private String title;

        private String content;

        private Username user;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class Username {
        private String username;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class AppealPostOwner {
        private UserWithProfileDTO.UserProfileInfo userProfileInfo;

        private String username;
    }
}
