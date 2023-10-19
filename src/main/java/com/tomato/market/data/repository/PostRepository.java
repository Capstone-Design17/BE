package com.tomato.market.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tomato.market.data.entity.PostEntity;

public interface PostRepository extends JpaRepository<PostEntity, Integer> {
	Page<PostEntity> findAll(Pageable pageable);

	Page<PostEntity> findByTitleContaining(String keyword, Pageable pageable);
	// findAll()? // int page
//	Optional<List<PostEntity>> find();

//	Optional<ImageEntity> findImageByPostNum(Integer postNum);
}
