package com.tomato.market.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.tomato.market.data.dto.ImageDto;
import com.tomato.market.data.dto.PostDto;

public interface BoardService {
	PostDto writePost(PostDto postDto);

	void uploadImages(MultipartFile[] files) throws IOException;

	List<PostDto> getPostList();

	ImageDto getPostImage(Integer postNum);

}
