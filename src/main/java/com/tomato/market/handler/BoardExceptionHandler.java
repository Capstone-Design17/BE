package com.tomato.market.handler;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tomato.market.controller.BoardController;
import com.tomato.market.data.dto.PostResponseDto;
import com.tomato.market.handler.exception.BoardException;

@RestControllerAdvice(basePackageClasses = BoardController.class)
public class BoardExceptionHandler {
	private final Logger logger = LoggerFactory.getLogger(BoardExceptionHandler.class);

	@ExceptionHandler(BoardException.class)
	public PostResponseDto handlePostException(BoardException exception) {
		logger.warn("BoardExceptionHandler.handlerPostException() is called");

		return PostResponseDto.builder()
			.status(HttpStatus.OK)
			.message(exception.getMessage())
			.build();
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public PostResponseDto handlePostValidation(MethodArgumentNotValidException exception) {
		logger.info("BoardExceptionHandler.handlePostValidation() is called");
		logger.warn("BoardExceptionHandler.handlePostValidation() : Validation 오류, 데이터 형식이 올바르지 않음");

		// Validation 에러 맵
		HashMap<String, String> validationErrorMap = new HashMap<>();
		exception.getBindingResult().getFieldErrors().forEach(error -> {
			validationErrorMap.put(error.getField(), error.getDefaultMessage());
		});

		PostResponseDto postResponseDto = PostResponseDto.builder()
			.status(HttpStatus.OK)
			.message(validationErrorMap)
			.build();

		return postResponseDto;
	}


}
