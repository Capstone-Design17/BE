package com.tomato.market.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tomato.market.data.dto.UserResponseDto;
import com.tomato.market.data.dto.UserSignUpDto;
import com.tomato.market.handler.exception.UserException;
import com.tomato.market.service.UserService;

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
	public String loginUser(@RequestBody UserSignUpDto userSignUpDto) { // Session 생성?
		UserSignUpDto loginResult = userService.loginUser(userSignUpDto);
		if (loginResult != null) { // 로그인 성공

		} else { // 로그인 실패

		}

		// 로그인 성공 or 실패 redirect
		return "로그인 성공"; // responseDto 반환할 것
	}

	@DeleteMapping("user/logout")
	public void logout() {
	}
}
