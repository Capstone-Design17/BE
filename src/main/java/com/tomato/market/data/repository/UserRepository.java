package com.tomato.market.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.tomato.market.data.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
	@Transactional
	boolean existsByEmail(String email); // Email을 통한 중복 체크

	@Transactional
	boolean existsById(String id);

	@Transactional
	boolean existsByPhone(String phone);

	@Transactional
	Optional<UserEntity> findById(String id);
}
