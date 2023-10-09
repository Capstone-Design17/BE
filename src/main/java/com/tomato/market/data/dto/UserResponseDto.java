package com.tomato.market.data.dto;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserResponseDto {
	private HttpStatus status;
	private Object message;
}
