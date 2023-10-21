package com.tomato.market.dao.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tomato.market.dao.BoardDao;
import com.tomato.market.data.entity.ImageEntity;
import com.tomato.market.data.entity.PostEntity;
import com.tomato.market.data.repository.ImageRepository;
import com.tomato.market.data.repository.PostRepository;
import com.tomato.market.handler.exception.BoardException;

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
	public Page<PostEntity> findPostList(Pageable pageable) {
		logger.info("BoardDaoImpl.findPostList() is called");

		Page<PostEntity> postEntities = postRepository.findAll(pageable); // 페이징 여부에 따라 수정될 여지 있음 // findPostAll
		if (postEntities != null) {
			logger.info("BoardDaoImpl.findPostList() : 데이터 목록 조회 성공");
			return postEntities;
		} else {
			logger.warn("BoardDaoImpl.findPostList() : 데이터 목록 조회 실패");
			throw new BoardException("데이터 목록을 불러오지 못했습니다.");
		}
	}

	@Override
	public Page<PostEntity> findPostSearchList(String keyword, Pageable pageable) {
		logger.info("BoardDaoImpl.findPostSearchList() is called");

		Page<PostEntity> postEntities = postRepository.findByTitleContaining(keyword, pageable);
		if (postEntities != null) {
			logger.info("BoardDaoImpl.findPostSearchList() : 검색 목록 조회 성공");
			return postEntities;
		} else {
			logger.warn("BoardDaoImpl.findPostSearchList() : 검색 목록 조회 실패");
			throw new BoardException("검색 결과 목록을 불러오지 못했습니다.");
		}
	}

	@Override
	public ImageEntity findImageByPostNum(Integer postNum) {
		logger.info("BoardDaoImpl.findImageByPostNum() is called");
//		Optional<ImageEntity> imageEntity = imageRepository.findImageByPostNum(postNum);
		Optional<ImageEntity> imageEntity = imageRepository.findTopByPostNumOrderByImageNum(postNum);
		if (imageEntity.isPresent()) { // PostId로 이미지를 찾음
			logger.info("BoardDaoImpl.findImageByPostNum() : 이미지 조회 성공");
			return imageEntity.get();
		} else { // 이미지를 찾지 못함 or 애초에 없음
			logger.warn("BoardDaoImpl.findImageByPostNum() : 이미지 조회 실패");
			return null;
		}
	}

	@Override
	public PostEntity findPostByPostNum(Integer postNum) {
		logger.info("BoardDaoImpl.findPostByPostNum() is called");

		Optional<PostEntity> postEntity = postRepository.findByPostNum(postNum);
		if (postEntity.isPresent()) {
			logger.info("BoardDaoImpl.findPostByPostNum() : 게시글 조회 성공");
			return postEntity.get();
		} else {
			logger.warn("BoardDaoImpl.findPostByPostNum() : 게시글 조회 실패");
			return null;
		}
	}

	@Override
	public List<ImageEntity> findImageListByPostNum(Integer postNum) {
		logger.info("BoarDaoImpl.findImageListByPostNum() is called");

		List<ImageEntity> imageEntities = imageRepository.findByPostNum(postNum);
		if (imageEntities == null) {
			logger.warn("BoarDaoImpl.findImageListByPostNum() : 이미지 리스트 조회 실패");
			return null;
		} else {
			logger.info("BoardDaoImpl.findImageListByPostNum() : 이미지 리스트 조회 성공");
			return imageEntities;
		}
	}
}
