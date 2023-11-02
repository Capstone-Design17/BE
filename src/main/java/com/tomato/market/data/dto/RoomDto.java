package com.tomato.market.data.dto;

import com.tomato.market.data.entity.RoomEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
	private Integer roomNum;
	private String roomId;
	private String sellerId;
	private String userId;
	private Integer postNum;

	public static RoomEntity toRoomEntity(RoomDto roomDto) {

		return RoomEntity.builder()
			.roomNum(roomDto.roomNum)
			.roomId(roomDto.roomId)
			.sellerId(roomDto.sellerId)
			.userId(roomDto.userId)
			.postNum(roomDto.postNum)
			.build();
	}

	public static RoomDto toRoomDto(RoomEntity roomEntity) {
		return RoomDto.builder()
			.roomNum(roomEntity.getRoomNum())
			.roomId(roomEntity.getRoomId())
			.sellerId(roomEntity.getSellerId())
			.userId(roomEntity.getUserId())
			.postNum(roomEntity.getPostNum())
			.build();
	}
}
