package com.spring.home_solver.repository;

import com.spring.home_solver.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {

    @Query("from UserProfile u where u.user.id = ?1 ")
    Optional<UserProfile> findByUserId(int userId);
}
