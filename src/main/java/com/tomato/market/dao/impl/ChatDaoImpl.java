package com.tomato.market.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tomato.market.dao.ChatDao;
import com.tomato.market.data.entity.RoomEntity;
import com.tomato.market.data.repository.RoomRepository;
import com.tomato.market.handler.exception.ChatException;

@Service
@Transactional
public class ChatDaoImpl implements ChatDao {
	private final Logger logger = LoggerFactory.getLogger(ChatDaoImpl.class);

	private RoomRepository roomRepository;

	@Autowired
	public ChatDaoImpl(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	@Override
	public RoomEntity findRoomNum(String userId, Integer postNum) {
		logger.info("ChatDaoImpl.findRoomNum() is called");

		RoomEntity roomEntity = roomRepository.findByUserIdAndPostNum(userId, postNum);
		if (roomEntity == null) {
			logger.info("ChatDaoImpl.findRoomNum() : roomNum을 찾지 못함");
			return null;
		} else {
			logger.info("ChatDaoImpl.findRoomNum() : roomNum을 찾음");
			return roomEntity;
		}

	}

	@Override
	public RoomEntity save(RoomEntity roomEntity) {
		logger.info("ChatDaoImpl.save() is called");
		RoomEntity result = roomRepository.save(roomEntity);
		if (result == null) {
			logger.warn("ChatDaoImpl.save() : 데이터 저장 실패");
			throw new ChatException("채팅방 생성에 실패했습니다.");
		}
		logger.info("ChatDaoImpl.save() : 데이터 저장 성공");
		return result;
	}
}
