package com.tomato.market.data.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Document(collation = "chat")
@Builder
@Getter
@ToString
public class ChatDocument {
	@Id
	private String id;
	private String roomId;
	private String sender;
	private String message;
	private String createdAt; // String
}
