package com.tomato.market.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.tomato.market.data.dto.FavoriteDto;
import com.tomato.market.data.dto.ImageDto;
import com.tomato.market.data.dto.PostDto;
import com.tomato.market.data.dto.SearchDto;

public interface BoardService {
	PostDto writePost(PostDto postDto);

	void uploadImages(Integer postNum, List<MultipartFile> files) throws IOException;

	Page<PostDto> getPostList(Pageable pageable);

	Page<PostDto> getPostSearchList(SearchDto searchDto, Pageable pageable);

	ImageDto getPostImage(Integer postNum);

	PostDto getPost(Integer postNum);

	List<ImageDto> getPostImageList(Integer postNum);

	FavoriteDto addFavorite(String userId, Integer postNum, Integer status);

	FavoriteDto getFavorite(String userId, Integer postNum);

	List<FavoriteDto> getFavoriteList(String userId);

	PostDto updatePost(PostDto postDto);

	PostDto updateStatus(PostDto postDto);

	List<PostDto> getSellList(String userId);
}
