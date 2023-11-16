package com.tomato.market.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.tomato.market.dao.impl.BoardDaoImpl;
import com.tomato.market.data.dto.FavoriteDto;
import com.tomato.market.data.dto.ImageDto;
import com.tomato.market.data.dto.PostDto;
import com.tomato.market.data.entity.FavoriteEntity;
import com.tomato.market.data.entity.ImageEntity;
import com.tomato.market.data.entity.PostEntity;
import com.tomato.market.handler.exception.BoardException;
import com.tomato.market.service.impl.BoardServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

	@Mock
	private BoardDaoImpl boardDao;

	// Post 입력값
	private Integer postNum = 1;
	private String userId = "spring";
	private String location = "노원구";
	private String title = "제목입니다.";
	private String category = "물건";
	private String content = "본문입니다.";
	private Integer price = 10000;
	private String detailLocation = "상계동";
	private Integer status = 0;
	private String boughtUserId = "";

	private PostDto postDto;
	private PostEntity postEntity;

	private ImageEntity imageEntity;
	private String imageName = "original.png";
	private String uuid = "uuidoriginal.png";


	private List<MultipartFile> files = new ArrayList<>();

	private List<PostEntity> postEntities;
	private List<ImageEntity> imageEntities;

	private Pageable pageable = PageRequest.of(0, 10);
	private Page<PostEntity> postEntityList;
	private String keyword = "keyword";
	private List<PostDto> postDtoList;

	private Page<PostDto> postDtoPage;

	// Favorite
	private FavoriteDto favoriteDto;
	private List<FavoriteDto> favoriteDtoList;
	private FavoriteEntity favoriteEntity;
	private List<FavoriteEntity> favoriteEntities;


	@BeforeEach
	void setUp() {
		postDto = PostDto.builder().postNum(postNum).userId(userId).location(location).title(title).category(category)
			.content(content)
			.price(price).detailLocation(detailLocation).status(status).boughtUserId(boughtUserId).build();

		postEntity = PostDto.toPostEntity(postDto);

		imageEntity = ImageEntity.builder()
			.postNum(postNum)
			.imageName(imageName)
			.uuid(uuid)
			.build();

		postEntities = new ArrayList<>();
		postEntities.add(postEntity);
		postEntities.add(postEntity);

		imageEntities = new ArrayList<>();
		imageEntities.add(imageEntity);
		imageEntities.add(imageEntity);

		postEntityList = new PageImpl<>(postEntities, pageable, 2);

		postDtoList = new ArrayList<>();
		postDtoList.add(PostDto.toPostDto(postEntity));
		postDtoList.add(PostDto.toPostDto(postEntity));

		postDtoPage = new PageImpl<>(postDtoList, pageable, 2);

		favoriteDto = FavoriteDto.builder()
			.userId(userId)
			.postNum(postNum)
			.status(1)
			.build();
		favoriteDtoList = new ArrayList<>();
		favoriteDtoList.add(favoriteDto);
		favoriteDtoList.add(favoriteDto);

		favoriteEntity = FavoriteDto.toFavoriteEntity(favoriteDto);
		favoriteEntities = new ArrayList<>();
		favoriteEntities.add(favoriteEntity);
		favoriteEntities.add(favoriteEntity);
	}

	@Test
	@DisplayName("게시글_등록_성공")
	void writePostSuccess() {
		given(boardDao.save(any(PostEntity.class))).willReturn(postEntity);

		BoardServiceImpl boardService = new BoardServiceImpl(boardDao);
		PostDto test1 = PostDto.toPostDto(postEntity);
		PostDto test2 = boardService.writePost(postDto);
		Assertions.assertEquals(test1.toString(), test2.toString());

		verify(boardDao).save(any(PostEntity.class));
	}

	@Test
	@DisplayName("게시글_데이터_저장_실패")
	void writePostFail() {
		given(boardDao.save(any(PostEntity.class))).willReturn(null);

		BoardServiceImpl boardService = new BoardServiceImpl(boardDao);
		assertThrows(BoardException.class, () -> {
			boardService.writePost(postDto);
		});

		verify(boardDao).save(any(PostEntity.class));
	}

	@Test
	@DisplayName("이미지_등록_성공")
	void uploadImageSuccess() throws IOException {
		MultipartFile mockFile1 = Mockito.mock(MultipartFile.class);
		MultipartFile mockFile2 = Mockito.mock(MultipartFile.class);
		doNothing().when(mockFile1).transferTo(any(File.class));
		doNothing().when(mockFile2).transferTo(any(File.class));
		given(boardDao.saveImage(any(ImageEntity.class))).willReturn(imageEntity);

		BoardServiceImpl boardService = new BoardServiceImpl(boardDao);
		boardService.uploadImages(postNum, List.of(mockFile1, mockFile2));

		verify(mockFile1).transferTo(any(File.class));
		verify(mockFile2).transferTo(any(File.class));
		verify(boardDao, times(2)).saveImage(any(ImageEntity.class));
	}

	@Test
	@DisplayName("이미지_파일_저장_실패")
	void saveImageFileFailure() throws IOException {
		MultipartFile mockFile = Mockito.mock(MultipartFile.class);
		// MultipartFile의 transferTo 메소드가 IOException을 던질 때를 시뮬레이트
		doThrow(BoardException.class).when(mockFile).transferTo(any(File.class));

		BoardServiceImpl boardService = new BoardServiceImpl(boardDao);
		BoardException exception = assertThrows(BoardException.class, () -> {
			boardService.uploadImages(postNum, List.of(mockFile));
		});
		Assertions.assertEquals(exception.getMessage(), "이미지 파일 저장에 실패했습니다.");

		// transferTo 메소드가 호출되었는지 확인
		verify(mockFile).transferTo(any(File.class));
	}

	@Test
	@DisplayName("이미지_정보_저장_실패")
	void saveImageInfoFailure() throws IOException {
		MultipartFile mockFile = Mockito.mock(MultipartFile.class);
		doNothing().when(mockFile).transferTo(any(File.class));
		given(boardDao.saveImage(any(ImageEntity.class))).willReturn(null);

		BoardServiceImpl boardService = new BoardServiceImpl(boardDao);
		BoardException exception = assertThrows(BoardException.class, () -> {
			boardService.uploadImages(postNum, List.of(mockFile));
		});

		Assertions.assertEquals(exception.getMessage(), "이미지 정보 저장에 실패했습니다.");

		verify(mockFile).transferTo(any(File.class));
		verify(boardDao).saveImage(any(ImageEntity.class));
	}

	@Test
	@DisplayName("게시글_리스트_조회_성공")
	void getPostListSuccess() {
		given(boardDao.findPostList(pageable)).willReturn(postEntityList);

		BoardServiceImpl boardService = new BoardServiceImpl(boardDao);

		Assertions.assertEquals(boardService.getPostList(pageable).toString(), postDtoPage.toString());

		verify(boardDao).findPostList(pageable);
	}

	@Test
	@DisplayName("게시글_리스트_조회_실패")
	void getPostListFailure() {
		given(boardDao.findPostList(pageable)).willReturn(null);

		BoardServiceImpl boardService = new BoardServiceImpl(boardDao);
		BoardException exception = Assertions.assertThrows(BoardException.class, () -> {
			boardService.getPostList(pageable);
		});
		Assertions.assertEquals(exception.getMessage(), "목록을 불러오지 못했습니다.");

		verify(boardDao).findPostList(pageable);
	}

	@Test
	@DisplayName("게시글_리스트_이미지_조회_성공")
	void getPostImageSuccess() {
		given(boardDao.findImageByPostNum(postNum)).willReturn(imageEntity);

		BoardServiceImpl boardService = new BoardServiceImpl(boardDao);
		Assertions.assertEquals(
			boardService.getPostImage(postNum).toString(), ImageDto.toImageDto(imageEntity).toString());

		verify(boardDao).findImageByPostNum(postNum);
	}

	@Test
	@DisplayName("게시글_리스트_이미지_조회_실패")
	void getPostImageFailure() {
		given(boardDao.findImageByPostNum(postNum)).willReturn(null);

		ImageEntity defaultImage = ImageEntity.builder()
			.postNum(postNum)
			.imageName("default.png")
			.uuid("default.png")
			.build();

		BoardServiceImpl boardService = new BoardServiceImpl(boardDao);
		Assertions.assertEquals(
			boardService.getPostImage(postNum).toString(), ImageDto.toImageDto(defaultImage).toString());

		verify(boardDao).findImageByPostNum(postNum);
	}

	@Test
	@DisplayName("게시글_리스트_검색_성공")
	void getPostSearchSuccess() {
		given(boardDao.findPostSearchList(any(String.class), any(Pageable.class))).willReturn(postEntityList);

		BoardServiceImpl boardService = new BoardServiceImpl(boardDao);
		Assertions.assertEquals(boardService.getPostSearchList(keyword, pageable).toString(), postDtoPage.toString());

		verify(boardDao).findPostSearchList(any(String.class), any(Pageable.class));
	}

	@Test
	@DisplayName("게시글_리스트_검색_실패")
	void getPostSearchFailure() {
		given(boardDao.findPostSearchList(any(String.class), any(Pageable.class))).willThrow(
			new BoardException("검색 결과 목록을 불러오지 못했습니다."));

		BoardServiceImpl boardService = new BoardServiceImpl(boardDao);
		BoardException exception = Assertions.assertThrows(BoardException.class, () -> {
			boardService.getPostSearchList(keyword, pageable);
		});
		Assertions.assertEquals(exception.getMessage(), "검색 결과 목록을 불러오지 못했습니다.");

		verify(boardDao).findPostSearchList(any(String.class), any(Pageable.class));
	}

	@Test
	@DisplayName("게시글_조회_성공")
	void getPostSuccess() {
		given(boardDao.findPostByPostNum(postNum)).willReturn(postEntity);

		BoardServiceImpl boardService = new BoardServiceImpl(boardDao);
		Assertions.assertEquals(PostDto.toPostDto(postEntity).toString(), boardService.getPost(postNum).toString());

		verify(boardDao).findPostByPostNum(postNum);
	}

	@Test
	@DisplayName("게시글_조회_실패")
	void getPostFailure() {
		given(boardDao.findPostByPostNum(postNum)).willReturn(null);

		BoardServiceImpl boardService = new BoardServiceImpl(boardDao);
		BoardException exception = Assertions.assertThrows(BoardException.class, () -> {
			boardService.getPost(postNum);
		});
		Assertions.assertEquals(exception.getMessage(), "게시글을 찾을 수 없습니다.");

		verify(boardDao).findPostByPostNum(postNum);
	}

	@Test
	@DisplayName("게시글_이미지_리스트_조회_성공")
	void getPostImageListSuccess() {
		given(boardDao.findImageListByPostNum(postNum)).willReturn(imageEntities);

		List<ImageDto> imageList = new ArrayList<>();
		for (ImageEntity image : imageEntities) {
			imageList.add(ImageDto.toImageDto(image));
		}

		BoardServiceImpl boardService = new BoardServiceImpl(boardDao);
		Assertions.assertEquals(imageList.toString(), boardService.getPostImageList(postNum).toString());

		verify(boardDao).findImageListByPostNum(postNum);
	}

	@Test
	@DisplayName("게시글_이미지_리스트_조회_실패")
	void getPostImageListFailure() {
		given(boardDao.findImageListByPostNum(postNum)).willReturn(null);

		BoardServiceImpl boardService = new BoardServiceImpl(boardDao);
		BoardException exception = Assertions.assertThrows(BoardException.class, () -> {
			boardService.getPostImageList(postNum);
		});
		Assertions.assertEquals(exception.getMessage(), "이미지를 불러오지 못했습니다.");

		verify(boardDao).findImageListByPostNum(postNum);
	}

	@Test
	@DisplayName("게시글_관심_등록_성공")
	void addFavoriteSuccess() {
		favoriteEntity.setStatus(1);
		favoriteDto.setStatus(1);
		given(boardDao.save(any(FavoriteEntity.class))).willReturn(favoriteEntity);

		BoardServiceImpl boardService = new BoardServiceImpl(boardDao);
		Assertions.assertEquals(boardService.addFavorite(userId, postNum, 0).toString(), favoriteDto.toString());

		verify(boardDao).save(any(FavoriteEntity.class));
	}

	@Test
	@DisplayName("게시글_관심_등록_실패")
	void addFavoriteFailure() {
		given(boardDao.findByUserIdAndPostNum(any(String.class), any(Integer.class))).willReturn(null);
		given(boardDao.save(any(FavoriteEntity.class))).willReturn(null);

		BoardServiceImpl boardService = new BoardServiceImpl(boardDao);
		BoardException exception = Assertions.assertThrows(BoardException.class, () -> {
			boardService.addFavorite(userId, postNum, 0);
		});
		Assertions.assertEquals(exception.getMessage(), "관심 등록에 실패했습니다.");

		verify(boardDao).findByUserIdAndPostNum(any(String.class), any(Integer.class));
		verify(boardDao).save(any(FavoriteEntity.class));
	}

	@Test
	@DisplayName("게시글_관심_등록_취소_성공")
	void cancelFavoriteSuccess() {
		favoriteEntity.setStatus(0);
		favoriteDto.setStatus(0);
		given(boardDao.findByUserIdAndPostNum(any(String.class), any(Integer.class))).willReturn(favoriteEntity);
		given(boardDao.save(any(FavoriteEntity.class))).willReturn(favoriteEntity);

		BoardServiceImpl boardService = new BoardServiceImpl(boardDao);
		Assertions.assertEquals(boardService.addFavorite(userId, postNum, 1).toString(), favoriteDto.toString());

		verify(boardDao).findByUserIdAndPostNum(any(String.class), any(Integer.class));
		verify(boardDao).save(any(FavoriteEntity.class));
	}

	@Test
	@DisplayName("게시글_관심_등록_확인_성공")
	void getFavoriteSuccess() {
		given(boardDao.findByUserIdAndPostNum(any(String.class), any(Integer.class))).willReturn(favoriteEntity);

		BoardServiceImpl boardService = new BoardServiceImpl(boardDao);
		Assertions.assertEquals(boardService.getFavorite(userId, postNum).toString(), favoriteDto.toString());

		verify(boardDao).findByUserIdAndPostNum(any(String.class), any(Integer.class));
	}

	@Test
	@DisplayName("게시글_관심_목록_조회_성공")
	void getFavoriteListSuccess() {
		given(boardDao.findByUserId(userId)).willReturn(favoriteEntities);

		BoardServiceImpl boardService = new BoardServiceImpl(boardDao);
		Assertions.assertEquals(boardService.getFavoriteList(userId).toString(), favoriteDtoList.toString());

		verify(boardDao).findByUserId(userId);
	}

	@Test
	@DisplayName("게시글_관심_목록_조회_실패")
	void getFavoriteListFailure() {
		given(boardDao.findByUserId(userId)).willReturn(null);

		BoardServiceImpl boardService = new BoardServiceImpl(boardDao);
		BoardException exception = Assertions.assertThrows(BoardException.class, () -> {
			boardService.getFavoriteList(userId);
		});
		Assertions.assertEquals(exception.getMessage(), "관심 목록 조회에 실패했습니다.");

		verify(boardDao).findByUserId(userId);
	}

	@Test
	@DisplayName("게시글_수정_성공")
	void updatePostSuccess() {
		postDto.setContent("수정된 내용");
		postEntity.setContent("수정된 내용");
		given(boardDao.save(any(PostEntity.class))).willReturn(postEntity);

		BoardServiceImpl boardService = new BoardServiceImpl(boardDao);
		Assertions.assertEquals(boardService.updatePost(postDto).toString(), postDto.toString());

		verify(boardDao).save(any(PostEntity.class));
	}

	@Test
	@DisplayName("게시글_수정_실패")
	void updatePostFailure() {
		given(boardDao.save(any(PostEntity.class))).willThrow(new BoardException("게시글 수정에 실패했습니다."));

		BoardServiceImpl boardService = new BoardServiceImpl(boardDao);
		BoardException exception = Assertions.assertThrows(BoardException.class, () -> {
			boardService.updatePost(postDto);
		});
		Assertions.assertEquals(exception.getMessage(), "게시글 수정에 실패했습니다.");

		verify(boardDao).save(any(PostEntity.class));
	}

	@Test
	@DisplayName("게시글_상태_수정_성공")
	void updateStatusSuccess() {
		given(boardDao.findPostByPostNum(any(Integer.class))).willReturn(postEntity);
		given(boardDao.save(any(PostEntity.class))).willReturn(postEntity);

		BoardServiceImpl boardService = new BoardServiceImpl(boardDao);
		Assertions.assertEquals(boardService.updateStatus(postDto).toString(), postDto.toString());

		verify(boardDao).findPostByPostNum(any(Integer.class));
		verify(boardDao).save(any(PostEntity.class));
	}

	@Test
	@DisplayName("게시글_상태_수정_실패")
	void updateStatusFailure() {
		given(boardDao.findPostByPostNum(any(Integer.class))).willReturn(postEntity);
		given(boardDao.save(any(PostEntity.class))).willThrow(new BoardException("게시글 상태 수정에 실패했습니다."));

		BoardServiceImpl boardService = new BoardServiceImpl(boardDao);
		BoardException exception = Assertions.assertThrows(BoardException.class, () -> {
			boardService.updateStatus(postDto);
		});
		Assertions.assertEquals(exception.getMessage(), "게시글 상태 수정에 실패했습니다.");

		verify(boardDao).findPostByPostNum(any(Integer.class));
		verify(boardDao).save(any(PostEntity.class));
	}

	@Test
	@DisplayName("판매_목록_조회_성공")
	void getSellListSuccess() {
		given(boardDao.findPostByUserId(any(String.class))).willReturn(postEntities);

		BoardServiceImpl boardService = new BoardServiceImpl(boardDao);
		Assertions.assertEquals(boardService.getSellList(userId).toString(), postDtoList.toString());

		verify(boardDao).findPostByUserId(any(String.class));
	}

	@Test
	@DisplayName("판매_목록_조회_실패")
	void getSellListFailure() {
		given(boardDao.findPostByUserId(any(String.class))).willReturn(null);

		BoardServiceImpl boardService = new BoardServiceImpl(boardDao);
		BoardException exception = Assertions.assertThrows(BoardException.class, () -> {
			boardService.getSellList(userId);
		});
		Assertions.assertEquals(exception.getMessage(), "판매 목록 조회에 실패했습니다.");

		verify(boardDao).findPostByUserId(any(String.class));
	}
}
