package com.spring.home_solver.service;

import com.spring.home_solver.DTO.CommentRequestDTO;
import com.spring.home_solver.DTO.CommentResponseDTO;
import com.spring.home_solver.entity.Comment;
import com.spring.home_solver.entity.Post;
import com.spring.home_solver.entity.User;
import com.spring.home_solver.exception.NotFoundExc;
import com.spring.home_solver.repository.CommentRepository;
import com.spring.home_solver.repository.PostRepository;
import com.spring.home_solver.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private ModelMapper modelMapper;

    private final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Transactional
    @Override
    public CommentResponseDTO createComment(CommentRequestDTO body, Integer postId) {

        User loginUser = authUtil.loginUser();

        Post existsPost = postRepository.findById(postId)
                .orElseThrow(()-> new NotFoundExc("post", "postId", postId));

        Comment newComment = new Comment(body.getContent()).setUser(loginUser).setPost(existsPost);

        commentRepository.save(newComment);

        CommentResponseDTO responseDTO = modelMapper.map(newComment, CommentResponseDTO.class);
        CommentResponseDTO.UserWhoComment userWhoComment = new CommentResponseDTO.UserWhoComment(loginUser.getId());
        responseDTO.setUserId(loginUser.getId());
        responseDTO.setPostId(existsPost.getId());
        responseDTO.setUser(userWhoComment);

        logger.info("created comment response ===> {}",responseDTO);
        return responseDTO;
    }

    @Override
    public void deleteCommentById(Integer commentId, Integer postId) {

        postRepository.findById(postId)
                .orElseThrow(()-> new NotFoundExc("post", "postId", postId));

        Comment existsComment = commentRepository.findByCommentIdAndPostId(commentId, postId);
        if(existsComment == null) throw new NotFoundExc("comment", "commentId", commentId);

        logger.info("found comment =====> {}",existsComment);

        commentRepository.deleteById(commentId);
    }
}
