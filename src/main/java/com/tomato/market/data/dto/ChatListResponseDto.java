package com.tomato.market.data.dto;

import org.springframework.http.HttpStatus;

import lombok.Builder;

@Builder
public class ChatListResponseDto {
	HttpStatus status;
	Object message;
	Object chatList;
}
