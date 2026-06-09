package com.spotlightsarajevo.modules.auth.domain;

import com.spotlightsarajevo.modules.auth.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDAO extends JpaRepository<UserEntity, Integer> {
    @Query("SELECT u FROM UserEntity u WHERE u.sysEmail = :email OR u.googleEmail = :email")
    Optional<UserEntity> findBySysEmailOrGoogleEmail(@Param("email") String email);

    Optional<UserEntity> findByGoogleId(String googleId);

    Optional<UserEntity> findBySysEmail(String email);

    Optional<UserEntity> findByGoogleEmail(String email);
}
