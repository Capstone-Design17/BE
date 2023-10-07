package com.tomato.market.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomato.market.data.dto.UserSignUpDto;
import com.tomato.market.handler.exception.UserException;
import com.tomato.market.service.impl.UserServiceImpl;

/*
 * UserController 단위 테스트
 */

@WebMvcTest(UserController.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserServiceImpl userService;

	private String email = "daejin@daejin.ac.kr";
	private String id = "spring";
	private String pwd = "spring@123";
	private String pwdCheck = "spring@123";
	private String name = "홍길동";
	private String nickName = "nick";
	private String phone = "01012345678";
	private Integer status = 0;
	private Date birth;
	private UserSignUpDto userSignUpDto;
	private String content = "";
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	@Autowired
	private WebApplicationContext ctx; // 인코딩

	@BeforeEach
	void setup() throws ParseException {
		birth = formatter.parse("2023/09/27 01:25:27");
		mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))  // 한글 깨짐 처리
			.build();

		userSignUpDto = UserSignUpDto.builder()
			.email(email)
			.id(id)
			.pwd(pwd)
			.pwdCheck(pwdCheck)
			.name(name)
			.nickName(nickName)
			.phone(phone)
			.status(status)
			.birth(birth)
			.build();
	}

	@Test
	@DisplayName("회원가입_성공")
	void registerUserSuccess() throws Exception { // 회원가입 테스트
		// service 계층의 동작을 가정
		given(userService.registerUser(userSignUpDto)).willReturn(userSignUpDto);

		// 입력받을 JSON
		content = new ObjectMapper().writeValueAsString(userSignUpDto);

		// 테스트 내용
		mockMvc.perform(post("/api/user/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(content)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().string("{\"status\":\"OK\",\"message\":\"회원가입 성공\"}"))
			.andDo(print());

		// 가정한 메소드가 실행되었는지 체크
		verify(userService).registerUser(userSignUpDto);
	}

	@Test
	@DisplayName("이메일_형식_불일치")
	void validationTest() throws Exception { // 입력값 형식 맞지 않음
		// content의 입력값을 일부러 맞지 않도록 설정
		email = "1";
		userSignUpDto.setEmail(email);

		content = new ObjectMapper().writeValueAsString(userSignUpDto);

		mockMvc.perform(post("/api/user/register")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message.email").exists()) // message: email: 존재
			.andDo(print());
	}


	// 비밀번호 입력-확인 테스트 필요
	@Test
	@DisplayName("비밀번호_불일치")
	void checkPasswordFail() throws Exception {
		pwdCheck = pwd + "1"; // 비밀번호 확인 변경
		userSignUpDto.setPwdCheck(pwdCheck);

		content = new ObjectMapper().writeValueAsString(userSignUpDto);

		mockMvc.perform(post("/api/user/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(content)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().string("{\"status\":\"OK\",\"message\":\"비밀번호가 일치하지 않습니다.\"}"))
			.andDo(print());
	}

	@Test
	@DisplayName("이메일_중복")
	void duplicatedEmailException() throws Exception { // 이메일 중복
		// 이미 같은 이메일이 저장되어있는 상황을 가정
		given(userService.registerUser(userSignUpDto)).willThrow(new UserException("이미 가입된 이메일입니다."));

		content = new ObjectMapper().writeValueAsString(userSignUpDto);

		// 같은 email을 사용하는 content를 다시 저장
		mockMvc.perform(post("/api/user/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(content)
			)
			.andExpect(status().isOk())
			.andExpect(content().string("{\"status\":\"OK\",\"message\":\"이미 가입된 이메일입니다.\"}"))
			.andDo(print());

		verify(userService).registerUser(userSignUpDto);
	}

	@Test
	@DisplayName("아이디_중복")
	void duplicatedIdException() throws Exception { // 아이디 중복
		// 이미 같은 아이디가 저장되어있는 상황을 가정
		given(userService.registerUser(userSignUpDto)).willThrow(new UserException("이미 가입된 아이디입니다."));

		content = new ObjectMapper().writeValueAsString(userSignUpDto);

		// 같은 아이디를 사용하는 content를 다시 저장
		mockMvc.perform(post("/api/user/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(content)
			)
			.andExpect(status().isOk())
			.andExpect(content().string("{\"status\":\"OK\",\"message\":\"이미 가입된 아이디입니다.\"}"))
			.andDo(print());

		verify(userService).registerUser(userSignUpDto);
	}

	@Test
	@DisplayName("전화번호_중복")
	void duplicatedPhoneException() throws Exception { // 전화번호 중복
		// 이미 같은 전화번호가 저장되어있는 상황을 가정
		given(userService.registerUser(userSignUpDto)).willThrow(new UserException("이미 가입된 전화번호입니다."));

		content = new ObjectMapper().writeValueAsString(userSignUpDto);

		// 같은 아이디를 사용하는 content를 다시 저장
		mockMvc.perform(post("/api/user/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(content)
			)
			.andExpect(status().isOk())
			.andExpect(content().string("{\"status\":\"OK\",\"message\":\"이미 가입된 전화번호입니다.\"}"))
			.andDo(print());

		verify(userService).registerUser(userSignUpDto);
	}
}
