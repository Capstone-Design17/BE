package com.tomato.market.data.dto;

import com.tomato.market.data.entity.RoomEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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
public class RoomDto {
	private Integer roomNum;
	private String roomId;
	@NotBlank(message = "SellerId는 필수 입력값입니다.")
	private String sellerId;
	@NotBlank(message = "UserId는 필수 입력값입니다.")
	private String userId;
	@Positive(message = "PostNum은 필수 입력값입니다.")
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
