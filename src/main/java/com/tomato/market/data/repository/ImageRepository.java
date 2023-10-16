package com.tomato.market.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tomato.market.data.entity.ImageEntity;

public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {
	ImageEntity findImageByPostNum(Integer postNum);
}
