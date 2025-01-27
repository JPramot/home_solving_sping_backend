package com.spring.home_solver.repository;

import com.spring.home_solver.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Integer> {

    @Query("from PostImage pi where pi.id in ?1")
    List<PostImage> findByIds(int[] postImageId);
}
