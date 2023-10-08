package com.tomato.market.data.dto;

import com.tomato.market.data.entity.PostEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
//@AllArgsConstructor
public class PostDto {
	// PostDto 하나로 처리가 맞는지?
	// PostDetailDto가 따로 필요?
	String postId;


	// 내부에 Image가 포함?


	public static PostEntity toPostEntity(PostDto postDto) {
		return PostEntity.builder().build();
	}

	public static PostDto toPostDto(PostEntity postEntity) {
		return PostDto.builder().build();
	}
}
