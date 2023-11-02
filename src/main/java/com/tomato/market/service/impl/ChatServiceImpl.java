package com.tomato.market.service.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tomato.market.dao.ChatDao;
import com.tomato.market.data.document.ChatDocument;
import com.tomato.market.data.dto.ChatDto;
import com.tomato.market.data.dto.RoomDto;
import com.tomato.market.data.entity.RoomEntity;
import com.tomato.market.handler.exception.ChatException;
import com.tomato.market.service.ChatService;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {

	private final Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);
	private ChatDao chatDao;

	@Autowired
	public ChatServiceImpl(ChatDao chatDao) {
		this.chatDao = chatDao;
	}

	@Override
	public RoomDto createRoom(RoomDto roomDto) {
		logger.info("ChatService.createRoom() is called");
		// Room
		// UserId와 PostId로 생성된 채팅방 확인
//		RoomEntity roomEntity = RoomDto.toRoomEntity(roomDto);
		RoomEntity result = chatDao.findRoomId(roomDto.getUserId(), roomDto.getPostNum());
		if (result == null) { // 생성된 방이 없음
			logger.info("ChatService.createRoom() : roomId를 찾지 못함, 새 room 생성");
			// 방 생성
			UUID uuid = UUID.randomUUID();
			String roomId =
				uuid + "+" + roomDto.getPostNum() + "_" + roomDto.getSellerId() + "_" + roomDto.getUserId();
			roomDto.setRoomId(roomId);
			result = chatDao.save(RoomDto.toRoomEntity(roomDto));
		}

		return RoomDto.toRoomDto(result);
	}

	@Override
	public void saveChat(ChatDto chatDto) {
		logger.info("ChatService.saveChat() is called");

		ChatDocument chatDocument = ChatDto.toChatDocument(chatDto);
		// 받은 Chat을 RoomId로 MongoDB에 저장
		ChatDocument result = chatDao.save(chatDocument);
		if (result == null) {
			logger.warn("ChatService.saveChat() : 채팅 저장 실패");
			throw new ChatException("채팅을 저장하지 못했습니다.");
		}

		logger.info("ChatService.saveChat() : 저장된 채팅 확인-" + result);

	}
}
