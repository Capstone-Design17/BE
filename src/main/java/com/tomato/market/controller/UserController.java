package com.tomato.market.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tomato.market.data.dto.UserSignUpDto;
import com.tomato.market.service.UserService;

import jakarta.validation.Valid;

@RestController
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
	public String registerUser(@Valid @RequestBody UserSignUpDto userSignUpDto) { // DTO로 Body값 받음, Valid로 검증
		logger.info("UserController.registerUser() is called");

		// User 저장
		UserSignUpDto registerResult = userService.registerUser(userSignUpDto);
		if (registerResult != null) { // 회원가입 성공
			logger.info("UserController.registerUser() : 회원가입 성공");
			return "회원가입 완료"; // redirect? or Status 200?
		} else { // 회원가입 실패
			logger.warn("UserController.registerUser() : 회원가입 실패");
			// 회원가입 실패의 이유를 받아야함
			return null; // 에러의 종류 반환 or Status 반환?
		}
	}

	@PostMapping("/user/login")
	public String loginUser(@RequestBody UserSignUpDto userSignUpDto) { // Session 생성?
		UserSignUpDto loginResult = userService.loginUser(userSignUpDto);
		if (loginResult != null) { // 로그인 성공

		} else { // 로그인 실패

		}

		// 로그인 성공 or 실패 redirect
		return "로그인 성공";
	}


}
