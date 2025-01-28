package com.spring.home_solver.service;

import com.spring.home_solver.DTO.*;
import com.spring.home_solver.DTO.postDTO.PostByIdResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {

    AllPostResponseDTO getAllPost();

    PostResponseDTO createPost(PostBodyDTO body, MultipartFile[] images);

    PostResponseDTO updatePost(PostBodyDTO body, MultipartFile[] images, Integer postId, String postImageId);

    PostByIdResponseDTO getPostByPostId(Integer postId);

    void deletePostById(Integer postId);

    ApiMessageResponse appealPost(Integer postId, AppealPostRequestDTO body);

    AllAppealPostResponseDTO getAllAppealPost();

    void deleteAppealPostById(Integer appealPostId);
}
