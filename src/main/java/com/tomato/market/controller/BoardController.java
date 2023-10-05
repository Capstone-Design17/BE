package com.tomato.market.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tomato.market.data.dto.UserResponseDto;
import com.tomato.market.service.BoardService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class BoardController {
	private final Logger logger = LoggerFactory.getLogger(BoardController.class);
	private final BoardService boardService;

	@Autowired
	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}

	@GetMapping("/board/getList")
	public UserResponseDto getList(HttpSession session) { // 리턴 타입 변경해야할 가능성 있음
		logger.info("BoardController.getList() is called");
		// 세션을 확인
		logger.info("BoardController.getList() : 세션 아이디-" + session.getId());
		logger.info("BoardController.getList() : 세션 값-" + session.getAttribute("userId"));

		if (session.getAttribute("userId") != null) { // 세션이 있는 경우
			logger.info("BoardController.getList() : 세션에 userId가 있음");
			// 있으면 List<EnrolEntity>?? 반환

			// (임시) 세션이 잘 들어왔는지 확인
			return UserResponseDto.builder()
				.status(HttpStatus.OK)
				.message(session.getAttribute("userId"))
				.build();
		} else { // 세션이 없는 경우
			// 없으면 유효하지 않은 세션입니다.
			logger.warn("BoardController.getList() : 세션에 userId가 없음");
			// throw new // 예외를 던지기
			// React에서 로그인 페이지로 Redirect
		}
		// 수정 예정
		return UserResponseDto.builder()
			.status(HttpStatus.BAD_REQUEST)
			.message("세션이 존재하지 않음")
			.build();
	}
}
