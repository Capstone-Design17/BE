package com.tomato.market.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tomato.market.data.entity.FavoriteEntity;
import com.tomato.market.data.entity.ImageEntity;
import com.tomato.market.data.entity.PostEntity;

public interface BoardDao {
	PostEntity save(PostEntity postEntity);

	ImageEntity saveImage(ImageEntity imageEntity);

	Page<PostEntity> findPostList(Pageable pageable);

	Page<PostEntity> findPostSearchList(String keyword, Pageable pageable);

	ImageEntity findImageByPostNum(Integer postNum);

	PostEntity findPostByPostNum(Integer postNum);

	List<ImageEntity> findImageListByPostNum(Integer postNum);

	FavoriteEntity save(FavoriteEntity favoriteEntity);
}
