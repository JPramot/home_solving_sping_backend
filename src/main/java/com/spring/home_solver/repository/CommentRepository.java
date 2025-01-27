package com.spring.home_solver.repository;

import com.spring.home_solver.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("from Comment c where c.id = ?1 and c.post.id = ?2")
    Comment findByCommentIdAndPostId(Integer commentId, Integer postId);
}
