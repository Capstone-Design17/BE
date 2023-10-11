package com.tomato.market.service.impl;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
		//유
		int count = 0;
		for (MultipartFile file : files) {
			logger.info("BoardServiceImpl.uploadImages() : 이미지" + (count + 1) + " 저장 시도");
			UUID uuid = UUID.randomUUID(); // UUID 생성
			String fileName = uuid + "_" + file.getOriginalFilename(); // 저장될 unique한 이름 생성 : originalName 형식 어떻게 되는지?
			logger.info("BoardServiceImpl.uploadImages() : fileName-" + fileName); // 파일 이름 형식 체크
			File savedFile = new File(projectPath, fileName); //

			try {
				file.transferTo(savedFile);
			} catch (Exception e) {
				logger.warn("BoardServiceImpl.uploadImages() : 파일 저장 실패");
				e.printStackTrace();
				throw new BoardException((count + 1) + "번째 이미지 저장에 실패했습니다.");
			}

//			 DB에 파일 정보 저장
			ImageEntity imageEntity =
				ImageEntity.builder().postNum(postNum).imageName(file.getOriginalFilename()).uuid(fileName).build();
			ImageEntity saveResult = boardDao.saveImage(imageEntity); // 어떤 식으로 저장되는지 repository 분리?
			if (imageEntity == null) {
				logger.warn("BoardServiceImpl.uploadImages() : DB에 정보 저장 실패");
				throw new BoardException((count + 1) + "번째 이미지 정보 저장에 실패했습니다.");
			} // 예외 처리
			logger.info("BoardServiceImpl.uploadImages() : DB에 정보 저장 성공");
		}

		logger.info("BoardServiceImpl.uploadImages() : 모든 이미지 저장 성공");
//		return null;
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
	public ImageDto getPostImage(Integer postNum) {
		// 게시글의 id로 image를 찾아 반환
		ImageEntity imageEntity = boardDao.findImageByPostNum(postNum);
		if (imageEntity == null) {
			// 이미지 없는 게시물
			// Default Image 반환
			imageEntity = ImageEntity.builder().build();
		}

		// Entity -> DTO 전환하여 반환
		return ImageDto.toImageDto(imageEntity);
	}
}
