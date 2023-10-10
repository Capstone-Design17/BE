package com.tomato.market.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tomato.market.dao.BoardDao;
import com.tomato.market.data.entity.ImageEntity;
import com.tomato.market.data.entity.PostEntity;
import com.tomato.market.data.repository.ImageRepository;
import com.tomato.market.data.repository.PostRepository;

@Service
@Transactional
public class BoardDaoImpl implements BoardDao {
	private final Logger logger = LoggerFactory.getLogger(BoardDaoImpl.class);

	private final PostRepository postRepository;
	private final ImageRepository imageRepository;

	@Autowired
	public BoardDaoImpl(PostRepository postRepository, ImageRepository imageRepository) {
		this.postRepository = postRepository;
		this.imageRepository = imageRepository;
	}


	@Override
	public PostEntity save(PostEntity postEntity) {
		logger.info("BoardDaoImpl.save() is called");
		// Entity로 Post 등록
		PostEntity savedResult = postRepository.save(postEntity);
		if (savedResult.getPostNum() != null) {
			logger.info("BoardDaoImpl.save() : 데이터 저장 성공");
			return savedResult;
		} else {
			logger.warn("BoardDaoImpl.save() : 데이터 저장 실패");
			return null;
		}
	}

	@Override
	public ImageEntity saveImage(ImageEntity imageEntity) {
		logger.info("BoardDaoImpl.saveImage() is called");

		ImageEntity savedResult = imageRepository.save(imageEntity);
		if (savedResult.getImageNum() != null) {
			logger.info("BoardDaoImpl.saveImage() : 이미지 정보 저장 성공");
			return savedResult;
		} else {
			logger.warn("BoardDaoImpl.saveImage() : 이미지 정보 저장 실패");
			return null;
		}
	}

	@Override
	public List<PostEntity> findPostList() {
//
//		Optional<List<PostEntity>> postEntities = postRepository.find();
//		if (postEntities.isPresent()) {
//			return postEntities.get();
//		} else {
//			throw new BoardException("데이터를 불러오지 못했습니다.");
//		}
		return null;
	}

	@Override
	public ImageEntity findImageByPostNum(Integer postNum) {
//		Optional<ImageEntity> imageEntity = postRepository.findImageByPostNum(postNum);
//		if (imageEntity.isPresent()) { // PostId로 이미지를 찾음
//			return imageEntity.get();
//		} else { // 이미지를 찾지 못함 or 애초에 없음?
//			// throw new ImageException();
//			// or return null?
		return null;
//		}
	}
}
