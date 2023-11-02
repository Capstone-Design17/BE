package com.tomato.market.data.dto;

import com.tomato.market.data.document.ChatDocument;

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

	public static ChatDocument toChatDocument(ChatDto chatDto) {
		return ChatDocument.builder()
			.roomId(chatDto.roomId)
			.sender(chatDto.sender)
			.message(chatDto.message)
			.createdAt(chatDto.createdAt)
			.build();
	}

	public static ChatDto toChatDto(ChatDocument chatDocument) {
		return ChatDto.builder()
			.roomId(chatDocument.getRoomId())
			.sender(chatDocument.getSender())
			.message(chatDocument.getMessage())
			.createdAt(chatDocument.getCreatedAt())
			.build();
	}
}
