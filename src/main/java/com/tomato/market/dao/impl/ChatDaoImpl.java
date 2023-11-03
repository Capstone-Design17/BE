package com.tomato.market.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tomato.market.dao.ChatDao;
import com.tomato.market.data.collection.ChatCollection;
import com.tomato.market.data.entity.RoomEntity;
import com.tomato.market.data.repository.ChatRepository;
import com.tomato.market.data.repository.RoomRepository;
import com.tomato.market.handler.exception.ChatException;

@Service
@Transactional
public class ChatDaoImpl implements ChatDao {
	private final Logger logger = LoggerFactory.getLogger(ChatDaoImpl.class);

	private RoomRepository roomRepository;
	private ChatRepository chatRepository;

	@Autowired
	public ChatDaoImpl(RoomRepository roomRepository, ChatRepository chatRepository) {

		this.roomRepository = roomRepository;
		this.chatRepository = chatRepository;
	}

	@Override
	public RoomEntity findRoomId(String userId, Integer postNum) {
		logger.info("ChatDaoImpl.findRoomId() is called");

		RoomEntity roomEntity = roomRepository.findByUserIdAndPostNum(userId, postNum);
		if (roomEntity == null) {
			logger.warn("ChatDaoImpl.findRoomId() : roomId를 찾지 못함");
			return null;
		} else {
			logger.info("ChatDaoImpl.findRoomId() : roomId를 찾음");
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

	@Override
	public ChatCollection save(ChatCollection chatCollection) {
		logger.info("ChatDaoImpl.save() is called");
		// MongoDB에 Chat 저장
		ChatCollection savedChat = chatRepository.save(chatCollection);
		if (savedChat == null) {
			logger.warn("ChatDaoImpl.save() : 채팅 저장 실패");
			return null;
		} else {
			logger.info("ChatDaoImpl.save() : 채팅 저장 성공");
			return savedChat;
		}
	}

	@Override
	public List<ChatCollection> findByRoomId(String roomId) {
		logger.info("ChatDaoImpl.findByRoomId() is called");

		List<ChatCollection> result = chatRepository.findByRoomId(roomId);
		logger.info("roomId: " + roomId);
		logger.info(result.toString());
		if (result == null) {
			logger.warn("ChatDaoImpl.findByRoomId() : 채팅 내역 조회 실패");
			return null;
		} else { // 채팅 내역이 없는 경우 포함, size == 0
			logger.info("ChatDaoImpl.findByRoomId() : 채팅 내역 조회 성공");
			return result;
		}
	}
}
