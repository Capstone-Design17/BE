package com.tomato.market.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tomato.market.data.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
	UserEntity findByEmail(String userEmail); // Email을 통한 중복 체크
}
