package com.tomato.market.data.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostListResponseDto {
	HttpStatus status;
	Object message;
	Object content;
}
