package com.tomato.market.dao.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomato.market.dao.BoardDao;
import com.tomato.market.data.entity.ImageEntity;
import com.tomato.market.data.entity.PostEntity;
import com.tomato.market.data.repository.PostRepository;
import com.tomato.market.handler.exception.BoardException;

@Service
public class BoardDaoImpl implements BoardDao {
	private final Logger logger = LoggerFactory.getLogger(BoardDaoImpl.class);

	private final PostRepository postRepository;

	@Autowired
	public BoardDaoImpl(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	@Override
	public PostEntity save(PostEntity postEntity) {
		logger.info("BoardDaoImpl.save() is called");
		// Entity로 Post 등록
		PostEntity savedResult = postRepository.save(postEntity);
		if (savedResult != null) { // 변경 있음
			logger.info("BoardDaoImpl.save() : 데이터 저장 성공");
			return savedResult;
		} else {
			logger.warn("BoardDaoImpl.save() : 데이터 저장 실패");
			return null;
		}
	}

	@Override
	public List<PostEntity> findPostList() {

		Optional<List<PostEntity>> postEntities = postRepository.find();
		if (postEntities.isPresent()) {
			return postEntities.get();
		} else {
			throw new BoardException("데이터를 불러오지 못했습니다.");
		}
	}

	@Override
	public ImageEntity findImageByPostId(String postId) {
		Optional<ImageEntity> imageEntity = postRepository.findImageByPostId(postId);
		if (imageEntity.isPresent()) { // PostId로 이미지를 찾음
			return imageEntity.get();
		} else { // 이미지를 찾지 못함 or 애초에 없음?
			// throw new ImageException();
			// or return null?
			return null;
		}
	}
}
