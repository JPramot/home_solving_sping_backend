package com.spring.home_solver.controller;

import com.spring.home_solver.DTO.CommentRequestDTO;
import com.spring.home_solver.DTO.CommentResponseDTO;
import com.spring.home_solver.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseEntity<CommentResponseDTO> createComment(@RequestBody CommentRequestDTO body,
                                                            @PathVariable Integer postId) {
        CommentResponseDTO responseDTO = commentService.createComment(body, postId);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @DeleteMapping("/{commentId}/{postId}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable Integer commentId,
                                                  @PathVariable Integer postId) {
        commentService.deleteCommentById(commentId,postId);
        return ResponseEntity.noContent().build();
    }

}
