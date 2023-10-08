package com.tomato.market.service;

import java.util.List;

import com.tomato.market.data.dto.ImageDto;
import com.tomato.market.data.dto.PostDto;

public interface BoardService {
	PostDto registerPost(PostDto postDto);

	List<PostDto> getPostList();

	ImageDto getPostImage(String postId);

}
