package com.tomato.market.data.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostListResponseDto {
	HttpStatus status;
	Object message;
	Object postList;
	Object imageList;
	Object page;
}
