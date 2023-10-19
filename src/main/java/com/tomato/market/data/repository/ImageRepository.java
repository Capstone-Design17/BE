package com.tomato.market.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tomato.market.data.entity.ImageEntity;

public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {
	Optional<ImageEntity> findImageByPostNum(Integer postNum);
}
