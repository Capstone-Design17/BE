package com.tomato.market.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tomato.market.data.entity.FavoriteEntity;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Integer> {
	FavoriteEntity findByUserIdAndPostNum(String userId, Integer postNum);

	List<FavoriteEntity> findByUserId(String userId);
}
