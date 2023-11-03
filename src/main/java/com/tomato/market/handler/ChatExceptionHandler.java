package com.tomato.market.handler;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tomato.market.controller.ChatController;
import com.tomato.market.data.dto.RoomResponseDto;
import com.tomato.market.handler.exception.ChatException;

@RestControllerAdvice(basePackageClasses = ChatController.class)
public class ChatExceptionHandler {
	private final Logger logger = LoggerFactory.getLogger(ChatExceptionHandler.class);

	@ExceptionHandler(ChatException.class)
	public RoomResponseDto handleChatException(ChatException exception) {
		logger.warn("ChatExceptionHandler.handleChatException() is called");

		return RoomResponseDto.builder()
			.status(HttpStatus.OK)
			.message(exception.getMessage())
			.build();
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public RoomResponseDto handleRoomValidation(MethodArgumentNotValidException exception) {
		logger.info("ChatExceptionHandler.handleRoomValidation() is called");
		logger.warn("ChatExceptionHandler.handleRoomValidation() : Validation 오류, 데이터 형식이 올바르지 않음");

		// Validation 에러 맵
		HashMap<String, String> validationErrorMap = new HashMap<>();
		exception.getBindingResult().getFieldErrors().forEach(error -> {
			validationErrorMap.put(error.getField(), error.getDefaultMessage());
		});

		RoomResponseDto roomResponseDto = RoomResponseDto.builder()
			.status(HttpStatus.OK)
			.message(validationErrorMap)
			.build();

		return roomResponseDto;
	}

}
