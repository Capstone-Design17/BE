package com.tomato.market.data.dto;

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
}
