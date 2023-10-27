package com.tomato.market.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
}
