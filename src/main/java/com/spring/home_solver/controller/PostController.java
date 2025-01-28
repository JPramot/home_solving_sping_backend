package com.spring.home_solver.controller;

import com.spring.home_solver.DTO.*;
import com.spring.home_solver.DTO.postDTO.PostByIdResponseDTO;
import com.spring.home_solver.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<AllPostResponseDTO> getAllPost() {
        return ResponseEntity.ok(postService.getAllPost());
    }

    @GetMapping("/{postId}/comment")
    public ResponseEntity<PostByIdResponseDTO> getPostByPostId(@PathVariable Integer postId) {
        PostByIdResponseDTO responseDTO = postService.getPostByPostId(postId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@ModelAttribute PostBodyDTO body,
                                                      @RequestPart(value = "image", required = false)
                                                      MultipartFile[] images) {
        PostResponseDTO responseDTO = postService.createPost(body, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> updatePost(@ModelAttribute PostBodyDTO body,
                                                      @RequestPart(value = "image", required = false)
                                                      MultipartFile[] images,
                                                      @PathVariable Integer postId,
                                                      @RequestPart(value = "deleteImage", required = false) String postImageId) {
        PostResponseDTO responseDTO = postService.updatePost(body, images, postId, postImageId);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePostById(@PathVariable Integer postId) {
        postService.deletePostById(postId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/appeal/{postId}")
    public ResponseEntity<ApiMessageResponse> appealPost(@PathVariable Integer postId,
                                                         @RequestBody AppealPostRequestDTO body) {
        ApiMessageResponse response = postService.appealPost(postId, body);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/appeal")
    public ResponseEntity<AllAppealPostResponseDTO> getAllAppealPost() {
        AllAppealPostResponseDTO responseDTO = postService.getAllAppealPost();
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/appeal/{appealPostId}")
    public ResponseEntity<Void> deleteAppealPost(@PathVariable Integer appealPostId) {
        postService.deleteAppealPostById(appealPostId);
        return ResponseEntity.noContent().build();
    }

}
