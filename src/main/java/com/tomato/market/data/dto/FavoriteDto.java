package com.tomato.market.data.dto;

import com.tomato.market.data.entity.FavoriteEntity;

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
public class FavoriteDto {
	private String userId;
	private Integer postNum;
	private Integer status;

	public static FavoriteDto toFavoriteDto(FavoriteEntity favoriteEntity) {
		return FavoriteDto.builder()
			.userId(favoriteEntity.getUserId())
			.postNum(favoriteEntity.getPostNum())
			.status(favoriteEntity.getStatus())
			.build();
	}

	public static FavoriteEntity toFavoriteEntity(FavoriteDto favoriteDto) {
		return FavoriteEntity.builder()
			.userId(favoriteDto.getUserId())
			.postNum(favoriteDto.getPostNum())
			.status(favoriteDto.getStatus())
			.build();
	}
}
