package com.tomato.market.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tomato.market.dao.ChatDao;
import com.tomato.market.data.dto.RoomDto;
import com.tomato.market.data.entity.RoomEntity;
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
		RoomEntity roomEntity = RoomDto.toRoomEntity(roomDto);
		RoomEntity result = chatDao.findRoomNum(roomDto.getUserId(), roomDto.getPostNum());
		if (result == null) { // 생성된 방이 없음
			logger.info("ChatService.createRoom() : room을 찾지 못함, 새 room 생성");
			// 방 생성
			result = chatDao.save(roomEntity);

			// 대화를 저장할 Text File 생성 : 상품아이디, 채팅방아이디 조합한 파일명
			// 추후 작업
		}

		return RoomDto.toRoomDto(result);
	}

}
