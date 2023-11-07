package com.tomato.market.data.dto;

import com.tomato.market.data.collection.ChatCollection;

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
public class ChatDto {
	//	private Integer chatNum;
	private String roomId;
	private String sender;
	private String message;
	private String createdAt; // String

	public static ChatCollection toChatCollection(ChatDto chatDto) {
		return ChatCollection.builder()
			.roomId(chatDto.roomId)
			.sender(chatDto.sender)
			.message(chatDto.message)
			.createdAt(chatDto.createdAt)
			.build();
	}

	public static ChatDto toChatDto(ChatCollection chatCollection) {
		return ChatDto.builder()
			.roomId(chatCollection.getRoomId())
			.sender(chatCollection.getSender())
			.message(chatCollection.getMessage())
			.createdAt(chatCollection.getCreatedAt())
			.build();
	}
}
