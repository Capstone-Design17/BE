package com.tomato.market.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tomato.market.data.dto.UserDto;
import com.tomato.market.service.UserService;

import jakarta.validation.Valid;

@RestController
public class UserController {
	/*	회원가입
	   	로그인
		로그아웃	*/

	@Autowired
	UserService userService;

	@PostMapping("/user/register")
	public String registerUser(@Valid @RequestBody UserDto userDto) {
		// DTO로 Body값 받음, Valid로 검증

		// User 저장
		UserDto registerResult = userService.registerUser(userDto);
		if (registerResult != null) { // 회원가입 성공
			return "회원가입 완료"; // redirect?
		} else { // 회원가입 실패
			return null; // 에러의 종류 반환
		}
	}

	@PostMapping("/user/login")
	public String loginUser(@RequestBody UserDto userDto) { // Session 생성?
		UserDto loginResult = userService.loginUser(userDto);
		if (loginResult != null) { // 로그인 성공

		} else { // 로그인 실패

		}

		// 로그인 성공 or 실패 redirect
		return "로그인 성공";
	}


}
