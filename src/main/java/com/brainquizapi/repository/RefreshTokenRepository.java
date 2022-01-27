package com.brainquizapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.brainquizapi.model.RefreshToken;
import com.brainquizapi.model.UserMasterEntity;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Override
    Optional<RefreshToken> findById(Long id);

    RefreshToken findByToken(String token);
    
    @Modifying
    int deleteByUser(UserMasterEntity user);

}