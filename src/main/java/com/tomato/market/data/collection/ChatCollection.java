package com.tomato.market.data.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Document(collection = "chat")
@Builder
@Getter
@ToString
public class ChatCollection {
	@Id
	private String id;
	private String roomId;
	private String sender;
	private String message;
	private String createdAt; // String
}
