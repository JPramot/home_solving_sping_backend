package com.spring.home_solver.service;

import com.spring.home_solver.DTO.CommentRequestDTO;
import com.spring.home_solver.DTO.CommentResponseDTO;

public interface CommentService {

    CommentResponseDTO createComment(CommentRequestDTO body, Integer postId);

    void deleteCommentById(Integer commentId, Integer postId);
}
