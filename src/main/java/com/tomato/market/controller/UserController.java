package com.tomato.market.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tomato.market.data.dto.ResponseDto;
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

	private final Logger logger = LoggerFactory.getLogger(UserController.class);
	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

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
			logger.info("UserController.loginUser() : 세션 아이디-" + session.getId());
			logger.info("UserController.loginUser() : 세션 값-" + session.getAttribute("userId"));
		}

		// 로그인 성공 응답 반환
		// Redirect는 React에서 할 예정
		logger.info("UserController.loginUser() : 로그인 성공");
		return UserResponseDto.builder().status(HttpStatus.OK).message("로그인 성공").build();
	}

	@GetMapping("/user/getSession")
	public UserResponseDto getUser(HttpSession session) { // 리턴 타입 변경해야할 가능성 있음
		logger.info("UserController.getUser() is called");
		// 세션을 확인
		logger.info("UserController.getUser() : 세션 아이디-" + session.getId());
		logger.info("UserController.getUser() : 세션 값-" + session.getAttribute("userId"));

		if (session.getAttribute("userId") == null) { // 세션이 없는 경우
			logger.warn("UserController.getUser() : 세션에 userId가 없음");
			throw new UserException("로그인이 필요합니다."); // 예외를 던지기
			// React에서 로그인 페이지로 Redirect
		}

		// 세션이 있는 경우
		logger.info("UserController.getUser() : 세션에 userId가 있음");

		// (임시) 세션의 User ID를 리턴
		return UserResponseDto.builder()
			.status(HttpStatus.OK)
			.message(session.getAttribute("userId"))
			.build();
	}

	@PostMapping("/user/logout")
	public UserResponseDto logout(HttpSession session) {
		logger.info("UserController.logout() is called");
		logger.info("UserController.logout() 세션 아이디 : " + session.getId());

		// 세션 무효
		session.invalidate(); // 값을 무효화?(초기화?)
		logger.info("UserController.logout() : 세션 invalidate");

		return UserResponseDto.builder()
			.status(HttpStatus.OK)
			.message("로그아웃 되었습니다.")
			.build();
	}

	// 닉네임 변경
	// PutMapping?
	@PutMapping("/user/nickname")
	public ResponseDto<String> updateNickname(String userId, String nickname) {
		logger.info("UserController.updateNickname() is called");

		// UserId의 Nickname을 수정
		// Service에서 -> DAO로 전달
		// DAO에서 userId를 업데이트하려면 UserDto가 필요?
		// 그러면 조회->수정 순서로 이루어져야 하나?
		String userNickname = userService.updateNickname(userId, nickname);
		logger.info("UserController.updateNickname() : 닉네임 변경 완료");

		// ReponseDto를 사용하기로 함, 기존에 작성된 코드는 시간관계상 그대로 두겠음
		return ResponseDto.<String>builder()
			.status(HttpStatus.OK)
			.message("닉네임 변경 성공")
			.data(userNickname)
			.build();
	}

	// 비밀번호 변경
	@PutMapping("/user/password")
	public ResponseDto<Object> updatePassword(String userId, String password, String newPassword) {
		logger.info("UserController.updatePassword() is called");

		userService.updatePassword(userId, password, newPassword);
		logger.info("UserController.updatePassword() : 비밀번호 변경 완료");

		return ResponseDto.<Object>builder()
			.status(HttpStatus.OK)
			.message("비밀번호 변경 성공")
			.build();
	}

	// 위치정보 변경
	@PutMapping("/user/location")
	public ResponseDto<String> updateLocation(String userId, String location) {
		logger.info("UserController.updateLocation() is called");
		logger.info(userId + " : " + location);

		String userLocation = userService.updateLocation(userId, location);
		logger.info("UserController.updateLocation() : 위치 번경 성공");

		return ResponseDto.<String>builder()
			.status(HttpStatus.OK)
			.message("위치 변경 성공")
			.data(userLocation)
			.build();
	}

	@GetMapping("/user/location")
	public ResponseDto<String> getLocation(String userId) {
		logger.info("UserController.getLocation() is called");

		String userLocation = userService.getLocation(userId);
		logger.info("UserController.getLocation() : 위치 조회 성공");
		String message = "위치 조회 성공";
		if (userLocation == null) {
			message = "등록된 위치정보 없음";
		}
		return ResponseDto.<String>builder()
			.status(HttpStatus.OK)
			.message(message)
			.data(userLocation)
			.build();
	}
}
