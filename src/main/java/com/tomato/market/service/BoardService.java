package com.tomato.market.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import com.tomato.market.data.dto.ImageDto;
import com.tomato.market.data.dto.PostDto;

public interface BoardService {
	PostDto writePost(PostDto postDto);

	void uploadImages(Integer postNum, List<MultipartFile> files) throws IOException;

	List<PostDto> getPostList();

	ImageDto getPostImage(Integer postNum);

	UrlResource getImageFile(ImageDto imageDto) throws MalformedURLException;
}
