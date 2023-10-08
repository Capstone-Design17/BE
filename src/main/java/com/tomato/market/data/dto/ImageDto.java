package com.tomato.market.data.dto;

import com.tomato.market.data.entity.ImageEntity;

import lombok.Builder;

@Builder
public class ImageDto {

	public static ImageDto toImageDto(ImageEntity imageEntity) {
		return ImageDto.builder().build();
	}
}
