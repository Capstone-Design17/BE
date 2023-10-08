package com.tomato.market.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomato.market.data.dto.UserLoginDto;
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

	private UserLoginDto userLoginDto;
	private String loginContent = "";

	@Autowired
	private WebApplicationContext ctx; // 인코딩

	@BeforeEach
	void setup() throws ParseException {
		birth = formatter.parse("2023/09/27 01:25:27");
		mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))  // 한글 깨짐 처리
			.build();

		// 회원가입용 객체
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

		// 로그인용 객체
		userLoginDto = UserLoginDto.builder()
			.id(id)
			.pwd(pwd)
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
	@DisplayName("회원가입_이메일_형식_불일치")
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
	@DisplayName("회원가입_비밀번호_불일치")
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
	@DisplayName("회원가입_이메일_중복")
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
	@DisplayName("회원가입_아이디_중복")
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
	@DisplayName("회원가입_전화번호_중복")
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

	@Test
	@DisplayName("로그인_성공")
	void loginUserSuccess() throws Exception {
		given(userService.loginUser(userLoginDto)).willReturn(userLoginDto);

		// View로부터 로그인 개체 받기
		loginContent = new ObjectMapper().writeValueAsString(userLoginDto);

		//
		mockMvc.perform(post("/api/user/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginContent)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().string("{\"status\":\"OK\",\"message\":\"로그인 성공\"}"))
			.andDo(print());

		verify(userService).loginUser(userLoginDto);
	}

	@Test
	@DisplayName("로그인_형식_불일치")
	void loginValidation() throws Exception {
		// 아이디 형식 불일치 가정
		userLoginDto.setId("sp");

		loginContent = new ObjectMapper().writeValueAsString(userLoginDto);

		mockMvc.perform(post("/api/user/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginContent)
				.accept(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk())
			.andExpect(jsonPath("$.message.id").exists()) // message: email: 존재
			.andDo(print());
	}

	@Test
	@DisplayName("로그인_아이디_조회_실패")
	void loginIdFail() throws Exception {
		given(userService.loginUser(userLoginDto)).willThrow(new UserException("등록되지 않은 아이디입니다."));

		loginContent = new ObjectMapper().writeValueAsString(userLoginDto);

		mockMvc.perform(post("/api/user/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginContent)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().string("{\"status\":\"OK\",\"message\":\"등록되지 않은 아이디입니다.\"}"))
			.andDo(print());

		verify(userService).loginUser(userLoginDto);
	}

	@Test
	@DisplayName("로그인_비밀번호_불일치")
	void loginPwdFail() throws Exception {
		given(userService.loginUser(userLoginDto)).willThrow(new UserException("비밀번호가 일치하지 않습니다."));

		// 비밀번호 불일치 설정
		userLoginDto.setPwd(userLoginDto.getPwd() + 1);

		loginContent = new ObjectMapper().writeValueAsString(userLoginDto);

		mockMvc.perform(post("/api/user/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginContent)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().string("{\"status\":\"OK\",\"message\":\"비밀번호가 일치하지 않습니다.\"}"))
			.andDo(print());

		verify(userService).loginUser(userLoginDto);
	}

	@Test
	@DisplayName("로그인_세션_존재")
	void existSession() throws Exception {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("userId", id);
		mockMvc.perform(get("/api/user/getSession").session(session))
			.andExpect(status().isOk())
			.andExpect(content().string("{\"status\":\"OK\",\"message\":\"" + session.getAttribute("userId") + "\"}"))
			.andDo(print());
	}

	@Test
	@DisplayName("로그인_세션_없음")
	void nonExistSession() throws Exception {
		MockHttpSession session = new MockHttpSession();
		mockMvc.perform(get("/api/user/getSession").session(session))
			.andExpect(status().isOk())
			.andExpect(content().string("{\"status\":\"OK\",\"message\":\"로그인이 필요합니다.\"}"))
			.andDo(print());
	}
}
