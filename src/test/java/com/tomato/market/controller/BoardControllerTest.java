package com.tomato.market.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomato.market.data.dto.FavoriteDto;
import com.tomato.market.data.dto.ImageDto;
import com.tomato.market.data.dto.PostDto;
import com.tomato.market.handler.exception.BoardException;
import com.tomato.market.service.impl.BoardServiceImpl;

@WebMvcTest(BoardController.class)
public class BoardControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BoardServiceImpl boardService;

	// Post 입력값
	private Integer postNum = 1;
	private String userId = "spring";
	private String location = "노원구";
	private String title = "제목입니다.";
	private String category = "1";
	private String content = "본문입니다.";
	private Integer price = 10000;
	private String detailLocation = "상계동";
	private Integer status = 0;
	private String boughtUserId = "";

	private PostDto postDto;
	private MockMultipartFile postFile;

	// Image 입력값
	private String fileName = "images";
	private List<MultipartFile> files = new ArrayList<>(); //
	private MockMultipartFile file1;
	private MockMultipartFile file2;

	private String postDtoJson = ""; // API 요청 body

	// PostList 반환값
	private List<PostDto> postList;
	private List<ImageDto> imageList;

	private ImageDto imageDto;
	private String imageName = "original.png";
	private String uuid = "uuidoriginal.png";

	// Page
	private Pageable pageable = PageRequest.of(0, 10);
	private Page<PostDto> postPageList;

	// Search
	private String keyword = "keyword";

	// Favorite
	private FavoriteDto favoriteDto;
	private List<FavoriteDto> favoriteDtoList;

	@Autowired
	private WebApplicationContext ctx;

	@BeforeEach
	void setUp() {
		mockMvc =
			MockMvcBuilders.webAppContextSetup(ctx)
				.addFilters(new CharacterEncodingFilter("UTF-8", true)).build();

		// 게시글 객체
		postDto = PostDto.builder()
			.postNum(postNum)
			.userId(userId)
			.location(location)
			.title(title)
			.category(category)
			.content(content)
			.price(price)
			.detailLocation(detailLocation)
			.status(status)
			.boughtUserId(boughtUserId)
			.build();

		// 이미지 객체
		file1 = new MockMultipartFile(fileName, "test1.png", MediaType.IMAGE_PNG_VALUE, "test1".getBytes());
		file2 = new MockMultipartFile(fileName, "test2.png", MediaType.IMAGE_PNG_VALUE, "test2".getBytes());

		files.add(file1);
		files.add(file2);

		// 게시글 리스트
		postList = new ArrayList<>();
		postList.add(postDto);
		postList.add(postDto);


		// 이미지 DB 정보
		imageDto = ImageDto.builder()
			.postNum(postNum)
			.imageName(imageName)
			.uuid(uuid)
			.build();

		// 이미지 리스트
		imageList = new ArrayList<>();
		imageList.add(imageDto);
		imageList.add(imageDto);

		// Page
		postPageList = new PageImpl<>(postList, pageable, 2);

		// Favorite
		favoriteDto = FavoriteDto.builder().userId(userId).postNum(postNum).status(1).build();
		favoriteDtoList = new ArrayList<>();
		favoriteDtoList.add(favoriteDto);
		favoriteDtoList.add(favoriteDto);
	}

	@Test
	@DisplayName("게시글_등록_성공")
	void writePostSuccess() throws Exception {
		given(boardService.writePost(any(PostDto.class))).willReturn(postDto);
		doNothing().when(boardService).uploadImages(postDto.getPostNum(), files); // void 반환 메소드에 대한 코드

		// Body 생성
		// PostDto와 MultiPartFile[]이 전달됨
		postDtoJson = new ObjectMapper().writeValueAsString(postDto); // postDto와, Image가 어떻게 전송되는지?
		postFile = new MockMultipartFile(
			"postDto", "", "application/json", postDtoJson.getBytes());
		try {
			mockMvc.perform(multipart("/api/board/writePost")
					.file(file1)
					.file(file2)
					.file(postFile)
					.contentType(MediaType.MULTIPART_FORM_DATA)
					.accept(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message", is("게시글 등록 성공")))
				.andDo(print());

		} catch (Exception exception) {
			exception.printStackTrace();
		}

		//
		verify(boardService).writePost(any(PostDto.class));
		verify(boardService).uploadImages(postDto.getPostNum(), files);
	}

	@Test
	@DisplayName("게시글_형식_불일치")
	void validationTest() throws Exception {
		// 제목이 입력되지 않은 상태 가정
		title = "";
		postDto.setTitle(title);

		postDtoJson = new ObjectMapper().writeValueAsString(postDto); // postDto와, Image가 어떻게 전송되는지?
		postFile = new MockMultipartFile(
			"postDto", "", "application/json", postDtoJson.getBytes());
		mockMvc.perform(multipart("/api/board/writePost")
				.file(file1)
				.file(file2)
				.file(postFile)
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message.title").exists()) // message: title 존재
			.andDo(print());
	}

	@Test
	@DisplayName("게시글_DTO_저장_실패")
	void writePostFailure() throws Exception {
		given(boardService.writePost(any(PostDto.class))).willThrow(new BoardException("게시글 등록에 실패했습니다."));

		postDtoJson = new ObjectMapper().writeValueAsString(postDto); // postDto와, Image가 어떻게 전송되는지?
		postFile = new MockMultipartFile(
			"postDto", "", "application/json", postDtoJson.getBytes());
		mockMvc.perform(multipart("/api/board/writePost")
				.file(file1)
				.file(file2)
				.file(postFile)
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message", is("게시글 등록에 실패했습니다.")))
			.andDo(print());

		//
		verify(boardService).writePost(any(PostDto.class));
	}

	@Test
	@DisplayName("게시글_이미지_저장_실패")
	void uploadImageFailure() throws Exception {
		given(boardService.writePost(any(PostDto.class))).willReturn(postDto);
		doThrow(new BoardException("1번째 이미지 저장에 실패했습니다."))
			.when(boardService).uploadImages(postDto.getPostNum(), files);

		postDtoJson = new ObjectMapper().writeValueAsString(postDto); // postDto와, Image가 어떻게 전송되는지?
		postFile = new MockMultipartFile(
			"postDto", "", "application/json", postDtoJson.getBytes());
		mockMvc.perform(multipart("/api/board/writePost")
				.file(file1)
				.file(file2)
				.file(postFile)
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message", is("1번째 이미지 저장에 실패했습니다.")))
			.andDo(print());

		//
		verify(boardService).writePost(any(PostDto.class));
		verify(boardService).uploadImages(postDto.getPostNum(), files);
	}

	@Test
	@DisplayName("게시글_이미지_정보_저장_실패")
	void saveImageInfoFailure() throws Exception {
		given(boardService.writePost(any(PostDto.class))).willReturn(postDto);
		doThrow(new BoardException("1번째 이미지 정보 저장에 실패했습니다."))
			.when(boardService).uploadImages(postDto.getPostNum(), files);

		postDtoJson = new ObjectMapper().writeValueAsString(postDto); // postDto와, Image가 어떻게 전송되는지?
		postFile = new MockMultipartFile(
			"postDto", "", "application/json", postDtoJson.getBytes());
		mockMvc.perform(multipart("/api/board/writePost")
				.file(file1)
				.file(file2)
				.file(postFile)
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message", is("1번째 이미지 정보 저장에 실패했습니다.")))
			.andDo(print());

		//
		verify(boardService).writePost(any(PostDto.class));
		verify(boardService).uploadImages(postDto.getPostNum(), files);
	}

	@Test
	@DisplayName("게시글_리스트_불러오기_성공")
	void getPostListSuccess() throws Exception {
		given(boardService.getPostList(any(Pageable.class))).willReturn(postPageList);
		given(boardService.getPostImage(postNum)).willReturn(imageDto);

		mockMvc.perform(get("/api/board/getPostList"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message", is("게시글 리스트 불러오기 성공")))
			.andExpect(jsonPath("$.postList").exists())
			.andExpect(jsonPath("$.imageList").exists())
			.andDo(print());

		verify(boardService).getPostList(any(Pageable.class));
		verify(boardService, times(2)).getPostImage(postNum);
	}

	@Test
	@DisplayName("게시글_리스트_게시글_불러오기_실패")
	void getPostListFailure() throws Exception {
		given(boardService.getPostList(any(Pageable.class))).willThrow(new BoardException("목록을 불러오지 못했습니다."));

		mockMvc.perform(get("/api/board/getPostList"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message", is("목록을 불러오지 못했습니다.")))
			.andDo(print());

		verify(boardService).getPostList(any(Pageable.class));
	}

	@Test
	@DisplayName("게시글_리스트_검색_성공")
	void getPostSearchListSuccess() throws Exception {
		given(boardService.getPostSearchList(any(String.class), any(Pageable.class))).willReturn(postPageList);
		given(boardService.getPostImage(postNum)).willReturn(imageDto);

		mockMvc.perform(get("/api/board/getPostList").param("keyword", "keyword"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message", is("게시글 리스트 불러오기 성공")))
			.andExpect(jsonPath("$.postList").exists())
			.andExpect(jsonPath("$.imageList").exists())
			.andDo(print());


		verify(boardService).getPostSearchList(any(String.class), any(Pageable.class));
		verify(boardService, times(2)).getPostImage(postNum);
	}

	@Test
	@DisplayName("게시글_리스트_검색_실패")
	void getPostSearchListFailure() throws Exception {
		given(boardService.getPostSearchList(any(String.class), any(Pageable.class))).willThrow(
			new BoardException("검색 결과 목록을 불러오지 못했습니다."));

		mockMvc.perform(get("/api/board/getPostList").param("keyword", "keyword"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message", is("검색 결과 목록을 불러오지 못했습니다.")))
			.andDo(print());

		verify(boardService).getPostSearchList(any(String.class), any(Pageable.class));
	}

	@Test
	@DisplayName("게시글_조회_성공")
	void getPostSuccess() throws Exception {
		given(boardService.getPost(postNum)).willReturn(postDto);
		given(boardService.getPostImageList(postNum)).willReturn(imageList);

		mockMvc.perform(get("/api/board/getPost").param("postNum", String.valueOf(postNum)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message", is("게시글 불러오기 성공")))
			.andExpect(jsonPath("$.postDto").exists())
			.andExpect(jsonPath("$.imageList").exists())
			.andDo(print());

		verify(boardService).getPost(postNum);
		verify(boardService).getPostImageList(postNum);
	}

	@Test
	@DisplayName("게시글_조회_실패")
	void getPostFailure() throws Exception {
		given(boardService.getPost(postNum)).willThrow(new BoardException("게시글을 찾을 수 없습니다."));

		mockMvc.perform(get("/api/board/getPost").param("postNum", String.valueOf(postNum)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message", is("게시글을 찾을 수 없습니다.")))
			.andDo(print());

		verify(boardService).getPost(postNum);

	}

	@Test
	@DisplayName("게시글_이미지_리스트_조회_실패")
	void getPostImageFailure() throws Exception {
		given(boardService.getPost(postNum)).willReturn(postDto);
		given(boardService.getPostImageList(postNum)).willThrow(new BoardException("이미지를 불러오지 못했습니다."));

		mockMvc.perform(get("/api/board/getPost").param("postNum", String.valueOf(postNum)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message", is("이미지를 불러오지 못했습니다.")))
			.andDo(print());

		verify(boardService).getPost(postNum);
		verify(boardService).getPostImageList(postNum);
	}

	@Test
	@DisplayName("게시글_관심_등록_성공")
	void addFavoriteSuccess() throws Exception {
		favoriteDto.setStatus(0);
		content = new ObjectMapper().writeValueAsString(favoriteDto);
		favoriteDto.setStatus(1);
		given(boardService.addFavorite(any(String.class), any(Integer.class), any(Integer.class))).willReturn(
			favoriteDto);


		mockMvc.perform(post("/api/board/favorite")
				.contentType(MediaType.APPLICATION_JSON)
				.content(content)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message", is("관심 등록 성공")))
			.andExpect(jsonPath("$.data.userId", is(userId)))
			.andDo(print());

		verify(boardService).addFavorite(any(String.class), any(Integer.class), any(Integer.class));
	}

	@Test
	@DisplayName("게시글_관심_등록_실패")
	void addFavoriteFailure() throws Exception {
		given(boardService.addFavorite(any(String.class), any(Integer.class), any(Integer.class))).willThrow(
			new BoardException("관심 등록에 실패했습니다."));

		content = new ObjectMapper().writeValueAsString(favoriteDto);

		mockMvc.perform(post("/api/board/favorite")
				.contentType(MediaType.APPLICATION_JSON)
				.content(content)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message", is("관심 등록에 실패했습니다.")))
			.andDo(print());

		verify(boardService).addFavorite(any(String.class), any(Integer.class), any(Integer.class));

	}

	@Test
	@DisplayName("게시글_관심_등록_취소_성공")
	void cancelFavoriteSuccess() throws Exception {
		content = new ObjectMapper().writeValueAsString(favoriteDto);
		favoriteDto.setStatus(0);
		given(boardService.addFavorite(any(String.class), any(Integer.class), eq(1))).willReturn(favoriteDto);

		mockMvc.perform(post("/api/board/favorite")
				.contentType(MediaType.APPLICATION_JSON)
				.content(content)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message", is("관심 등록 취소 성공")))
			.andDo(print());

		verify(boardService).addFavorite(any(String.class), any(Integer.class), eq(1));
	}

	@Test
	@DisplayName("게시글_관심_등록_확인_성공")
	void getFavoriteSuccess() throws Exception {
		given(boardService.getFavorite(userId, postNum)).willReturn(favoriteDto);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("userId", userId);
		map.add("postNum", postNum.toString());
		mockMvc.perform(get("/api/board/favorite").params(map))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message", is("관심 등록 확인 성공")))
			.andDo(print());

		given(boardService.getFavorite(userId, postNum)).willReturn(favoriteDto);
	}

	@Test
	@DisplayName("게시글_관심_등록_확인_실패")
	void getFavoriteFailure() throws Exception {
		given(boardService.getFavorite(userId, postNum))
			.willThrow(new BoardException("관심 등록 조회에 실패했습니다."));

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("userId", userId);
		map.add("postNum", postNum.toString());
		mockMvc.perform(get("/api/board/favorite").params(map))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message", is("관심 등록 조회에 실패했습니다.")))
			.andDo(print());

		verify(boardService).getFavorite(userId, postNum);
	}

	@Test
	@DisplayName("게시글_관심_목록_조회_성공")
	void getFavoriteListSuccess() throws Exception {
		given(boardService.getFavoriteList(userId)).willReturn(favoriteDtoList);

		mockMvc.perform(get("/api/board/favorite/list").param("userId", userId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message", is("관심 목록 조회 성공")))
			.andDo(print());

		verify(boardService).getFavoriteList(userId);
	}

	@Test
	@DisplayName("게시글_관심_목록_조회_실패")
	void getFavoriteListFailure() throws Exception {
		given(boardService.getFavoriteList(userId)).willThrow(new BoardException("관심 목록 조회에 실패했습니다."));

		mockMvc.perform(get("/api/board/favorite/list").param("userId", userId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message", is("관심 목록 조회에 실패했습니다.")))
			.andDo(print());

		verify(boardService).getFavoriteList(userId);
	}
}
