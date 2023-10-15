package com.tomato.market.data.dto;

import com.tomato.market.data.entity.PostEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostDto {

	Integer postNum;
	@Pattern(message = "잘못된 아이디 형식입니다.", regexp = "^[a-z0-9_-]{6,20}")
	String userId; // userId는 세션에서 들고옴
	@NotBlank(message = "지역을 입력하세요.")
	String location;
	@NotBlank(message = "제목을 입력하세요.")
	String title;
	String category; // Enum 클래스로 제한?
	@NotBlank(message = "내용을 입력하세요.")
	String content;
	@Positive(message = "숫자만 입력 가능합니다.")
	Integer price;
	@NotBlank(message = "거래희망 장소를 입력하세요. ")
	String detailLocation;
	Integer status; // 판매 상태 : 이것도 Enum?
	String boughtUserId;


	public static PostEntity toPostEntity(PostDto postDto) {

		return PostEntity.builder()
			.userId(postDto.getUserId())
			.location(postDto.getLocation())
			.title(postDto.getTitle())
			.category(postDto.getCategory())
			.content(postDto.getContent())
			.price(postDto.getPrice())
			.detailLocation(postDto.getDetailLocation())
			.status(postDto.getStatus())
			.boughtUserId(postDto.getBoughtUserId())
			.build();
	}

	public static PostDto toPostDto(PostEntity postEntity) {

		return PostDto.builder()
			.postNum(postEntity.getPostNum())
			.userId(postEntity.getUserId())
			.location(postEntity.getLocation())
			.title(postEntity.getTitle())
			.category(postEntity.getCategory())
			.content(postEntity.getContent())
			.price(postEntity.getPrice())
			.detailLocation(postEntity.getDetailLocation())
			.status(postEntity.getStatus())
			.boughtUserId(postEntity.getBoughtUserId())
			.build();
	}
}
