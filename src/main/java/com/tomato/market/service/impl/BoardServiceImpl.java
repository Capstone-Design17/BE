package com.tomato.market.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tomato.market.dao.BoardDao;
import com.tomato.market.data.dto.ImageDto;
import com.tomato.market.data.dto.PostDto;
import com.tomato.market.data.entity.ImageEntity;
import com.tomato.market.data.entity.PostEntity;
import com.tomato.market.handler.exception.BoardException;
import com.tomato.market.service.BoardService;

@Service
@Transactional
public class BoardServiceImpl implements BoardService {
	private final Logger logger = LoggerFactory.getLogger(BoardServiceImpl.class);

	private final BoardDao boardDao;

	@Value("${image.path}")
	private String projectPath; // 이미지 경로 얻기 : profile

	@Autowired
	public BoardServiceImpl(BoardDao boardDao) {
		this.boardDao = boardDao;
	}

	@Override
	public PostDto writePost(PostDto postDto) {
		logger.info("BoardServiceImpl.writePost() is called");

		// DTO -> Entity 전환
		PostEntity postEntity = PostDto.toPostEntity(postDto);

		logger.info("BoardServiceImpl.writePost() : 게시글 등록 시도");
		PostEntity saveResult = boardDao.save(postEntity);
		if (saveResult == null) { // 등록 실패
			logger.warn("BoardServiceImpl.writePost() : 게시글 등록 실패");
			throw new BoardException("게시글 등록에 실패했습니다.");
		}

		// 반환
		logger.info("BoardServiceImpl.writePost() : 게시글 등록 성공");
		return PostDto.toPostDto(saveResult);
	}

	@Override
	public void uploadImages(Integer postNum, List<MultipartFile> files) { // 이미지 업로드
		logger.info("BoardServiceImpl.uploadImages() is called");

		int count = 1;
		for (MultipartFile file : files) {
			logger.info("BoardServiceImpl.uploadImages() : 이미지" + (count++) + " 저장 시도");
			UUID uuid = UUID.randomUUID(); // UUID 생성
			String fileName = uuid + "_" + file.getOriginalFilename(); // 저장될 unique한 이름 생성 : originalName 형식 어떻게 되는지?
			logger.info("BoardServiceImpl.uploadImages() : fileName-" + fileName); // 파일 이름 형식 체크
			File savedFile = new File(projectPath, fileName); //

			try {
				file.transferTo(savedFile);
			} catch (Exception e) {
				logger.warn("BoardServiceImpl.uploadImages() : 파일 저장 실패");
				e.printStackTrace();
				throw new BoardException("이미지 파일 저장에 실패했습니다.");
			}
			logger.info("BoardServiceImpl.uploadImages() : 이미지 파일 저장 성공");

			// DB에 파일 정보 저장
			ImageEntity imageEntity =
				ImageEntity.builder().postNum(postNum).imageName(file.getOriginalFilename()).uuid(fileName).build();
			ImageEntity saveResult = boardDao.saveImage(imageEntity); // 어떤 식으로 저장되는지 repository 분리?
			if (saveResult == null) {
				logger.warn("BoardServiceImpl.uploadImages() : DB에 정보 저장 실패");
				throw new BoardException("이미지 정보 저장에 실패했습니다.");
			} // 예외 처리
			logger.info("BoardServiceImpl.uploadImages() : DB에 정보 저장 성공");
		}

		logger.info("BoardServiceImpl.uploadImages() : 모든 이미지 저장 성공");
	}

	@Override
	public Page<PostDto> getPostList(Pageable pageable) { // 페이징 예정
		logger.info("BoardServiceImpl.getPostList() is called");

		Page<PostEntity> postEntities = boardDao.findPostList(pageable);
		if (postEntities == null) {
			// 예외처리
			logger.warn("BoardServiceImpl.getPostList() : 포스트 목록 조회 실패");
			throw new BoardException("목록을 불러오지 못했습니다.");
		}
		logger.info("BoardServiceImpl.getPostList() : 포스트 목록 조회 성공");

		// Entity -> DTO 전환 후 List에 추가
		Page<PostDto> postList = postEntities.map(PostDto::toPostDto);

		return postList;
	}

	@Override
	public Page<PostDto> getPostSearchList(String keyword, Pageable pageable) {
		logger.info("BoardServiceImpl.getPostSearchList() is called");

		Page<PostEntity> postEntities = boardDao.findPostSearchList(keyword, pageable);
		if (postEntities == null) {
			logger.warn("BoardServiceImpl.getPostSearchList() : 검색 결과 목록 조회 실패");
			throw new BoardException("검색 결과 목록을 불러오지 못했습니다.");
		}
		logger.info("BoardServiceImpl.getPostSearchList() : 검색 결과 목록 조회 성공");

		Page<PostDto> postList = postEntities.map(PostDto::toPostDto);
		return postList;
	}

	@Override
	public ImageDto getPostImage(Integer postNum) {
		logger.info("BoardServiceImpl.getPostImage() is called");
		// 게시글의 id로 image를 찾아 반환
		ImageEntity imageEntity = boardDao.findImageByPostNum(postNum); // 1개만 받는 메소드

		// 이미지가 없는 경우, Default 이미지 전송
		if (imageEntity == null) { // 이미지 없는 게시물
			logger.info("BoardServiceImpl.getPostImage() : 이미지가 없는 포스트");
			// Default Image 반환
			imageEntity = ImageEntity.builder()
				.postNum(postNum)
				.imageName("default.png")
				.uuid("default.png")
				.build();
		} else {
			logger.info("BoardServiceImpl.getPostImage() : 이미지가 있는 포스트");
		}

		// Entity -> DTO 전환하여 반환
		return ImageDto.toImageDto(imageEntity);
	}

	@Override
	public PostDto getPost(Integer postNum) {
		logger.info("BoardServiceImpl.getPost() is called");

		PostEntity postEntity = boardDao.findPostByPostNum(postNum);
		if (postEntity == null) {
			logger.warn("BoardServiceImpl.getPost() : 게시글 조회 실패");
			throw new BoardException("게시글을 찾을 수 없습니다.");
		}
		logger.info("BoardServiceImpl.getPost() : 게시글 조회 성공");

		// Entity -> DTO 전환
		return PostDto.toPostDto(postEntity);
	}

	@Override
	public List<ImageDto> getPostImageList(Integer postNum) {
		logger.info("BoardServiceImpl.getPostImageList() is called");

		List<ImageEntity> imageEntities = boardDao.findImageListByPostNum(postNum);
		if ((imageEntities == null)) { // 이미지가 없는 데이터라면?
			logger.warn("BoardServiceImpl.getPostImageList() : 이미지 조회 실패");
			throw new BoardException("이미지를 불러오지 못했습니다.");
		}

		if (imageEntities.size() == 0) {
			logger.warn("BoardServiceImpl.getPostImageList() : 이미지가 0개인 게시글");
		}

		// Entity -> DTO 변환
		List<ImageDto> imageList = new ArrayList<>();
		for (ImageEntity imageEntity : imageEntities) {
			imageList.add(ImageDto.toImageDto(imageEntity));
		}
		return imageList;
	}
}
