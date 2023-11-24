package com.tomato.market.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tomato.market.data.entity.PostEntity;

public interface PostRepository extends JpaRepository<PostEntity, Integer> {
	Page<PostEntity> findAll(Pageable pageable);

	Page<PostEntity> findByTitleContaining(String keyword, Pageable pageable);

	Page<PostEntity> findByCategoryContaining(String keyword, Pageable pageable);

	Page<PostEntity> findByLocationContaining(String keyword, Pageable pageable);

	Optional<PostEntity> findByPostNum(Integer postNum);

	List<PostEntity> findByUserId(String userId);

}
