package com.tomato.market.handler;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tomato.market.controller.UserController;
import com.tomato.market.data.dto.UserResponseDto;
import com.tomato.market.handler.exception.UserException;

@RestControllerAdvice(basePackageClasses = UserController.class)
public class UserExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(UserExceptionHandler.class);

	// throw한 예외를 처리
	@ExceptionHandler(UserException.class)
	public UserResponseDto handleUserException(UserException exception) {
		logger.warn("UserExceptionHandler.handleUserException() is called");


		// 특정 타입(DTO)로 예외를 반환
		return UserResponseDto.builder()
			.status(HttpStatus.OK)
			.message(exception.getMessage())
			.build();

		// 현재 모든 응답을 200으로 처리, body의 메시지를 보고 Client에서 처리하는 구조
		// 400번대 Status로 변환 필요 : 409 Conflict
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public UserResponseDto handleUserValidation(MethodArgumentNotValidException exception) {
		logger.info("UserExceptionHandler.handleUserValidation() is called");
		logger.warn("UserExceptionHandler.handleUserValidation() : Validation 오류, 데이터 형식이 올바르지 않음");

		// Validation 에러 맵
		HashMap<String, String> validationErrorMap = new HashMap<>();
		exception.getBindingResult().getFieldErrors().forEach(error -> {
			validationErrorMap.put(error.getField(), error.getDefaultMessage());
		});

		UserResponseDto userResponseDto = UserResponseDto.builder()
			.status(HttpStatus.OK)
			.message(validationErrorMap)
			.build();

		return userResponseDto;
	}
}
