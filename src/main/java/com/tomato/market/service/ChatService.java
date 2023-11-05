package com.tomato.market.service;

import java.util.List;

import com.tomato.market.data.dto.ChatDto;
import com.tomato.market.data.dto.RoomDto;

public interface ChatService {
	public RoomDto createRoom(RoomDto roomDto);

	public void saveChat(ChatDto chatDto);

	public List<ChatDto> getChatList(String roomId);
}
