package com.tomato.market.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tomato.market.data.dto.UserLoginDto;
import com.tomato.market.data.dto.UserResponseDto;
import com.tomato.market.data.dto.UserSignUpDto;
import com.tomato.market.handler.exception.UserException;
import com.tomato.market.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {
	/*	회원가입
	   	로그인
		로그아웃	*/

	private final Logger logger = LoggerFactory.getLogger(UserController.class);
	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}


//	@GetMapping("/user/register") // react에서 처리?

	@PostMapping("/user/register")
	public UserResponseDto registerUser(@RequestBody @Valid UserSignUpDto userSignUpDto) { // DTO로 Body값 받음, Valid로 검증
		logger.info("UserController.registerUser() is called");
		logger.info("UserController.registerUser() : Validation 오류 체크 성공");

		// 비밀번호 확인
		if (!userSignUpDto.getPwd().equals(userSignUpDto.getPwdCheck())) {
			logger.info("UserController.registerUser() : 비밀번호 불일치");
			throw new UserException("비밀번호가 일치하지 않습니다.");
		}
		logger.info("UserController.registerUser() : 비밀번호 일치 확인");

		// User 저장
		UserSignUpDto registerResult = userService.registerUser(userSignUpDto);

		// 회원가입 성공 및 결과 반환
		logger.info("UserController.registerUser() : 회원가입 성공");
		return UserResponseDto.builder().status(HttpStatus.OK).message("회원가입 성공").build();
	}

	@PostMapping("/user/login")
	public UserResponseDto loginUser(@RequestBody @Valid UserLoginDto userLoginDto, HttpSession session) {
		// 메소드 호출 시점에 Session을 서블릿 컨테이너로부터 전달
		// 사용자의 쿠키에 Session ID가 포함되어 있다면 해당 Session 전달, 없으면 새로 생성 : getSession(), Default 30분
		// Session에 접근 시 유효시간 자동 초기화

		logger.info("UserController.loginUser() : is called");
		logger.info("UserController.loginUser() : Validation 오류 체크 성공");

		// 서비스 호출, DTO 넘김
		// 현재 사용할 데이터가 없기 떄문에 UserLoginDto를 사용하지만 추후 Sessio에 담을 정보에 따라 변동 가능 있음
		UserLoginDto loginResult = userService.loginUser(userLoginDto); // LoginDto 말고 UserDto로 받기?


		if (loginResult != null) { // 로그인 성공
			// 세션에 값 저장 : 세션의 담을 데이터의 범위는 어디까지?
			session.setAttribute("userId", loginResult.getId()); // 세션 구분은 sessionId로 하는듯?
			// 세션을 Redis에 저장?
			// 어떻게 저장하고 접근하는지?
		}

		// 로그인 성공 응답 반환
		// Redirect는 React에서 할 예정
		return UserResponseDto.builder().status(HttpStatus.OK).message("로그인 성공").build();
	}
}
