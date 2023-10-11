package com.tomato.market.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
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
	private List<MultipartFile> files = new ArrayList<>(); // 어떻게 입력값을 생성?
	private MockMultipartFile file1;
	private MockMultipartFile file2;

	private String postDtoJson = ""; // API 요청 body

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
			.price(price).detailLocation(detailLocation).status(status).boughtUserId(boughtUserId).build();

		// 이미지 객체
		file1 = new MockMultipartFile(fileName, "test1.png", MediaType.IMAGE_PNG_VALUE, "test1".getBytes());
		file2 = new MockMultipartFile(fileName, "test2.png", MediaType.IMAGE_PNG_VALUE, "test2".getBytes());

		files.add(file1);
		files.add(file2);

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
		mockMvc.perform(multipart("/api/board/writePost")
				.file(file1)
				.file(file2)
				.file(postFile)
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().string("{\"status\":\"OK\",\"message\":\"게시글 등록 성공\"}"))
			.andDo(print());

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
	void writePostFailed() throws Exception {
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
			.andExpect(content().string("{\"status\":\"OK\",\"message\":\"게시글 등록에 실패했습니다.\"}"))
			.andDo(print());

		//
		verify(boardService).writePost(any(PostDto.class));
	}

	@Test
	@DisplayName("게시글_이미지_저장_실패")
	void uploadImageFailed() throws Exception {
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
			.andExpect(content().string("{\"status\":\"OK\",\"message\":\"1번째 이미지 저장에 실패했습니다.\"}"))
			.andDo(print());

		//
		verify(boardService).writePost(any(PostDto.class));
		verify(boardService).uploadImages(postDto.getPostNum(), files);
	}

	@Test
	@DisplayName("게시글_이미지_정보_저장_실패")
	void saveImageInfoFailed() throws Exception {
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
			.andExpect(content().string("{\"status\":\"OK\",\"message\":\"1번째 이미지 정보 저장에 실패했습니다.\"}"))
			.andDo(print());

		//
		verify(boardService).writePost(any(PostDto.class));
		verify(boardService).uploadImages(postDto.getPostNum(), files);
	}
}
