package com.tomato.market.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tomato.market.data.dto.FavoriteDto;
import com.tomato.market.data.dto.ImageDto;
import com.tomato.market.data.dto.PageDto;
import com.tomato.market.data.dto.PostDto;
import com.tomato.market.data.dto.PostListResponseDto;
import com.tomato.market.data.dto.PostResponseDto;
import com.tomato.market.data.dto.ResponseDto;
import com.tomato.market.service.BoardService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class BoardController {
	private final Logger logger = LoggerFactory.getLogger(BoardController.class);

	private final BoardService boardService;

	@Autowired
	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}


	// 계획을 수정
	// 기본 등록 메소드 부터 만든 후
	// 조회 메소드를 만들기로 함
	@PostMapping(value = "/board/writePost", consumes = {MediaType.APPLICATION_JSON_VALUE,
		MediaType.MULTIPART_FORM_DATA_VALUE})
	public PostResponseDto writePost(
		@RequestPart(value = "postDto") @Valid PostDto postDto,
		@RequestPart(value = "images", required = false) List<MultipartFile> files)
		throws IOException { // 추후 @Valid // 이미지도 받아와야 함
		logger.info("BoardController.writePost() is called");
		logger.info("BoardController.writerPost() : Validation 검증 성공");

		// 게시글 등록
		PostDto savedPost = boardService.writePost(postDto);
		logger.info("BoardController.writePost() : 게시글 저장 성공");

		Integer postNum = savedPost.getPostNum(); //

		if (files != null) {
			logger.info("BoardController.writePost() : 이미지 개수-" + files.size());
			// postID와 연관하여 이미지 등록 // Image API를 따로 분리? : DB간 관계가 성립되지 않는 문제 있음
			boardService.uploadImages(postNum, files);
			logger.info("BoardController.writePost() : 이미지 저장 성공");
		}

		// 결과 리턴
		return PostResponseDto.builder()
			.status(HttpStatus.OK)
			.message("게시글 등록 성공")
			.build();
	}

	// 조회 삭제 수정 : 각각 별도 이슈로 분리
//	PutMapping?
//	updatePost() {}


	@GetMapping(value = "/board/getPostList")
	public PostListResponseDto getPostList(
		@PageableDefault(page = 0, size = 10, sort = "postNum", direction = Sort.Direction.DESC) Pageable pageable,
		@RequestParam(required = false) String keyword) throws MalformedURLException {
		logger.info("BoardController.getPostList() is called");
//		logger.info("BoardController.getPostList() page : " + pageable.getPageNumber());

		// 게시글 리스트를 받음
		Page<PostDto> postList = null;
		if (keyword == null) {
			logger.info("BoardController.getPostList() : 검색 키워드 없음");
			postList = boardService.getPostList(pageable);
		} else {
			logger.info("BoardController.getPostList() : 검색 키워드 있음");
			postList = boardService.getPostSearchList(keyword, pageable);
		}


		logger.info("BoardController.getPostList() : 게시글 리스트를 찾음");

		int nowPage = postList.getPageable().getPageNumber() + 1;
		int startPage = Math.max(nowPage - 2, 1);
		int endPage = Math.min(nowPage + 2, postList.getTotalPages());
		int totalPage = postList.getTotalPages();

		PageDto pageDto = PageDto.builder()
			.nowPage(nowPage)
			.startPage(startPage)
			.endPage(endPage)
			.totalPage(totalPage)
			.build();


		// 찾은 postList에서 각 Post의 ID로 Image를 찾음
		List<ImageDto> imageList = new ArrayList<>();
		for (PostDto postDto : postList) {
			// 썸네일로 사용할 Image 1개만 필요
			imageList.add(boardService.getPostImage(postDto.getPostNum()));
		}
		logger.info("BoardController.getPostList() : 게시글의 이미지 정보를 찾음");

		// Return
		// 게시글 List 첨부
		// 이미지 List 첨부
		return PostListResponseDto.builder()
			.status(HttpStatus.OK)
			.message("게시글 리스트 불러오기 성공")
			.postList(postList)
			.imageList(imageList)
			.page(pageDto)
			.build();
	}

	@GetMapping("/board/getPost") // {id} 형태로 받는게 나았을 수도?
	public PostResponseDto getPost(Integer postNum) { // 게시글 조회
		logger.info("BoardController.getPost() is called");

		// 특정 값(postNum)을 받아 그 내용을 조회
		PostDto postDto = boardService.getPost(postNum);
		logger.info("BoardController.getPost() : 게시글 불러오기 성공");


		// postNum으로 Image 데이터(다수) 조회
		List<ImageDto> imageList = boardService.getPostImageList(postNum);
		// 애초에 Post에 image 포함 여부 항목이 있었어야 했다.. // 전부 수정하는 것은 너무 복잡
		logger.info("BoardController.getPost() : 이미지 리스트 불러오기 성공");


		// return값으로 postDto, imageList 전달
		return PostResponseDto.builder()
			.status(HttpStatus.OK)
			.message("게시글 불러오기 성공")
			.postDto(postDto)
			.imageList(imageList)
			.build();
	}

	// 관심등록
	/* flow
		PostDetail에서 버튼을 클릭
		관심등록(좋아요 +1), 재클릭시 취소(삭제) -> 삭제말고 status로 하는게?
		각 사용자당 각각 처리되어야 함
		UserEntity에 ManyToOne : 다 대 일 연관관계 매핑?
		별도 Table로 분리?
			UserId, PostNum
	 */


	// 관심 등록/취소
	@PostMapping("/board/favorite")
	public ResponseDto<FavoriteDto> addFavorite(@RequestBody FavoriteDto favoriteDto) {
		logger.info("BoardController.addFavorite() is called");

		// status가 "on"이면 현재 등록된 상태
		FavoriteDto result = boardService.addFavorite(favoriteDto.getUserId(), favoriteDto.getPostNum(),
			favoriteDto.getStatus());
		String message = "";
		if (result.getStatus() == 1) {
			message = "관심 등록 성공";
		} else {
			message = "관심 등록 취소 성공";
		}
		logger.info(result.toString());

		// 좋아요 전체 개수를 리턴? // boardEntity 자체를 수정?
		return ResponseDto.<FavoriteDto>builder().status(HttpStatus.OK).message(message).data(result)
			.build();
	}

	// 관심 등록 확인
	@GetMapping("/board/favorite")
	public ResponseDto<FavoriteDto> getFavorite(String userId, Integer postNum) {
		logger.info("BoardController.getFavorite() is called");

		FavoriteDto favoriteDto = boardService.getFavorite(userId, postNum);
		logger.info("BoardController.getFavorite() : 관심 등록 조회 성공");

		return ResponseDto.<FavoriteDto>builder()
			.status(HttpStatus.OK)
			.message("관심 등록 확인 성공")
			.data(favoriteDto)
			.build();
	}

	@GetMapping("/board/favorite/list")
	public ResponseDto<PostListResponseDto> getFavoriteList(String userId) {
		logger.info("BoardController.getFavoriteList() is called");

		// 관심 목록 번호 조회
		List<FavoriteDto> favoriteDtoList = boardService.getFavoriteList(userId);
		logger.info("BoardController.getFavoriteList() : 관심 목록 조회 성공");

		// 관심 목록에 대한 PostList 조회
		// fovoriteList에 대한 가공 -> 조회
		List<PostDto> postDtoList = new ArrayList<>();
		for (FavoriteDto favoriteDto : favoriteDtoList) {
			postDtoList.add(boardService.getPost(favoriteDto.getPostNum()));
		}

		// PostList에 대한 ImageList 조회
		List<ImageDto> imageDtoList = new ArrayList<>();
		for (PostDto postDto : postDtoList) {
			imageDtoList.add(boardService.getPostImage(postDto.getPostNum()));
		}

		PostListResponseDto responseDto = PostListResponseDto.builder()
			.postList(postDtoList)
			.imageList(imageDtoList)
			.build();

		return ResponseDto.<PostListResponseDto>builder()
			.status(HttpStatus.OK)
			.message("관심 목록 조회 성공")
			.data(responseDto) // 어떤 형식으로 넘기지?
			.build();
	}


	// 게시글 내용 변경
	@PutMapping("/board/post") // null 값 그대로 전달
	public ResponseDto<PostDto> updatePost(@RequestBody PostDto postDto) {
		logger.info("BoardController.updatePost() is called");

		// 게시글 수정
		PostDto result = boardService.updatePost(postDto);

		// 이미지 수정은 어떻게?

		logger.info("BoardController.updatePost() : 게시글 수정 성공");
		return ResponseDto.<PostDto>builder()
			.status(HttpStatus.OK)
			.message("게시글 수정 성공")
			.data(postDto)
			.build();
	}

	// 판매 상태 변경
	// Patch?
	@PatchMapping("board/post")
	public ResponseDto<PostDto> updateStatus(@RequestBody PostDto postDto) {
		logger.info("BoardController.updateStatus() is called");

		PostDto result = boardService.updateStatus(postDto);

		logger.info("BoardController.updateStatus() : 게시글 상태 수정 성공");
		return ResponseDto.<PostDto>builder()
			.status(HttpStatus.OK)
			.message("게시글 상태 수정 성공")
			.data(result)
			.build();
	}
}
