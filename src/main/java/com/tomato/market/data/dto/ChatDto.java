package com.tomato.market.data.dto;

import java.util.Date;

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
public class ChatDto {
	private Integer chatNum;
	private Integer roomNum;
	private String sender;
	private String message;
	private Date createdAt; // String
}
