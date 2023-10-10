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
public class PostDto {

	Integer userNum; // 관계를 가지는 키 값을 어떻게 구축?
	String location;
	String title;
	String category; // Enum 클래스로 제한?
	String content;
	Integer price;
	String detailLocation;
	Integer status; // 판매 상태 : 이것도 Enum?
	//	Date createdAt; // DTO에서는 필요 없을지도
	Integer boughtUser;


	public static PostEntity toPostEntity(PostDto postDto) {

		return PostEntity.builder()
			.userNum(postDto.getUserNum())
			.location(postDto.getLocation())
			.title(postDto.getTitle())
			.category(postDto.getCategory())
			.content(postDto.getContent())
			.price(postDto.getPrice())
			.detailLocation(postDto.getDetailLocation())
			.status(postDto.getStatus())
			.boughtUser(postDto.getBoughtUser())
			.build();
	}

	public static PostDto toPostDto(PostEntity postEntity) {

		return PostDto.builder()
			.userNum(postEntity.getUserNum())
			.location(postEntity.getLocation())
			.title(postEntity.getTitle())
			.category(postEntity.getCategory())
			.content(postEntity.getContent())
			.price(postEntity.getPrice())
			.detailLocation(postEntity.getDetailLocation())
			.status(postEntity.getStatus())
			.boughtUser(postEntity.getBoughtUser())
			.build();
	}
}
