package com.tomato.market.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tomato.market.data.entity.PostEntity;

public interface PostRepository extends JpaRepository<PostEntity, Integer> {
	// findAll()? // int page
//	Optional<List<PostEntity>> find();

//	Optional<ImageEntity> findImageByPostNum(Integer postNum);
}
