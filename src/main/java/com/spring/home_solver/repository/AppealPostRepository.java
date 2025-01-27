package com.spring.home_solver.repository;

import com.spring.home_solver.entity.AppealPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppealPostRepository extends JpaRepository<AppealPost, Integer> {
}
