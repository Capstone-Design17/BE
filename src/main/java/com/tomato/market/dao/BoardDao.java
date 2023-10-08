package com.tomato.market.dao;

import java.util.List;

import com.tomato.market.data.entity.ImageEntity;
import com.tomato.market.data.entity.PostEntity;

public interface BoardDao {
	PostEntity save(PostEntity postEntity);

	List<PostEntity> findPostList();

	ImageEntity findImageByPostId(String postId);

}
