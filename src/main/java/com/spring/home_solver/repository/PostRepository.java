package com.spring.home_solver.repository;

import com.spring.home_solver.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("from Post p left join fetch p.user u " +
            "left join fetch u.userProfile " +
            "left join fetch p.comments c " +
            "left join fetch p.postImages pi " +
            "order by p.createdAt desc ")
    List<Post> findAllPost();

    @Query("from Post p " +
            "left join fetch p.user pu " +
            "left join fetch pu.userProfile " +
            "left join fetch p.postImages " +
            "where p.id = ?1")
    Post findPostById(Integer id);
}
