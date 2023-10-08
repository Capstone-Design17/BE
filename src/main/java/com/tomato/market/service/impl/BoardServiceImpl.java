package com.tomato.market.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomato.market.dao.BoardDao;
import com.tomato.market.data.dto.ImageDto;
import com.tomato.market.data.dto.PostDto;
import com.tomato.market.data.entity.ImageEntity;
import com.tomato.market.data.entity.PostEntity;
import com.tomato.market.service.BoardService;

@Service
public class BoardServiceImpl implements BoardService {
	private final Logger logger = LoggerFactory.getLogger(BoardServiceImpl.class);

	private final BoardDao boardDao;

	@Autowired
	public BoardServiceImpl(BoardDao boardDao) {
		this.boardDao = boardDao;
	}

	@Override
	public PostDto registerPost(PostDto postDto) {
		logger.info("BoardServiceImpl.registerPost() is called");
		// DTO -> Entity 전환
		PostEntity postEntity = PostDto.toPostEntity(postDto);

		// 검증 코드가 필요한가?

		PostEntity saveResult = boardDao.save(postEntity);
		if (saveResult == null) { // 등록 실패
			// throw new Exception();
		}


		// 반환
		return PostDto.toPostDto(saveResult);
	}

	@Override
	public List<PostDto> getPostList() {
		// getPostList의 범위
		// 한번에 모든 데이터를 불러오는 것은 무리가 있음
		// 한 페이지 당으로 제약해서 get?

		List<PostEntity> postEntities = boardDao.findPostList();
		if (postEntities != null) {
			// 예외처리
		}

		// get한 정보를 어떻게 리스트로?
		// Entity -> DTO 전환
		return null;
	}

	@Override
	public ImageDto getPostImage(String postId) {
		// 게시글의 id로 image를 찾아 반환
		ImageEntity imageEntity = boardDao.findImageByPostId(postId);
		if (imageEntity == null) {
			// 이미지 없는 게시물
			// Default Image 반환
			imageEntity = ImageEntity.builder().build();
		}

		// Entity -> DTO 전환하여 반환
		return ImageDto.toImageDto(imageEntity);
	}
}
