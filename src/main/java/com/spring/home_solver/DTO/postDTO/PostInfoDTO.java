package com.spring.home_solver.DTO.postDTO;

import com.spring.home_solver.DTO.PostImageInfo;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PostInfoDTO {

    private Integer id;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private Boolean isDelete;

    private Boolean canComment;

    private UserInPost user;

    private Set<CommentInfo> comments;

    private Set<PostImageInfo> postImages;

    private Integer userId;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class CommentInfo {
        private String content;
    }


}
