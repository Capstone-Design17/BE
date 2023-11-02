package com.tomato.market.service;

import com.tomato.market.data.dto.ChatDto;
import com.tomato.market.data.dto.RoomDto;

public interface ChatService {
	public RoomDto createRoom(RoomDto roomDto);

	public void saveChat(ChatDto chatDto);
}
