package com.tomato.market.data.dto;

import com.tomato.market.data.entity.ImageEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class ImageDto {
	Integer imageNum;
	Integer postNum;
	String imageName;
	String uuid;

	public static ImageDto toImageDto(ImageEntity imageEntity) {

		return ImageDto.builder()
			.imageNum(imageEntity.getImageNum())
			.postNum(imageEntity.getPostNum())
			.imageName(imageEntity.getImageName())
			.uuid(imageEntity.getUuid())
			.build();
	}
}
