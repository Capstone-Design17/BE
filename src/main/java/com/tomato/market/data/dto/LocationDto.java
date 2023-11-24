package com.tomato.market.data.dto;

import com.tomato.market.data.entity.LocationEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LocationDto {
	private Integer locationNum;
	private String userId;
	private String location;

	static LocationDto toLocationDto(LocationEntity locationEntity) {
		return LocationDto.builder()
			.locationNum(locationEntity.getLocationNum())
			.userId(locationEntity.getUserId())
			.location(locationEntity.getLocation())
			.build();
	}

	static LocationEntity toLocationEntity(LocationDto locationDto) {
		return LocationEntity.builder()
			.locationNum(locationDto.getLocationNum())
			.userId(locationDto.getUserId())
			.location(locationDto.getLocation())
			.build();
	}

}
