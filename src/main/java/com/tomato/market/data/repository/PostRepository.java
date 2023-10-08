package com.tomato.market.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tomato.market.data.entity.ImageEntity;
import com.tomato.market.data.entity.PostEntity;

public interface PostRepository extends JpaRepository<PostEntity, Integer> {
	// findAll()? // int page
	Optional<List<PostEntity>> find();

	Optional<ImageEntity> findImageByPostId(String postId);
}
