package com.tomato.market.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tomato.market.dao.impl.ChatDaoImpl;
import com.tomato.market.data.collection.ChatCollection;
import com.tomato.market.data.dto.ChatDto;
import com.tomato.market.data.dto.RoomDto;
import com.tomato.market.data.entity.RoomEntity;
import com.tomato.market.handler.exception.ChatException;
import com.tomato.market.service.impl.ChatServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {

	@Mock
	ChatDaoImpl chatDao;

	// Room
	private String roomId = "uuid-seller-buyer";
	private String sellerId = "seller";
	private String userId = "buyer";
	private Integer postNum = 50;
	private RoomDto roomDto;
	private RoomEntity roomEntity;

	// ChatList
	private List<ChatDto> chatList;
	private String message = "Hello World!";
	private String createdAt = "2023/11/10 01:03:08";

	// Chat
	private ChatDto chatDto;
	private String sender = "sender";

	// ChatCollection
	private ChatCollection chatCollection;
	private List<ChatCollection> chatCollectionList;

	@BeforeEach
	void setUp() {
		roomDto = RoomDto.builder()
			.roomId(roomId)
			.sellerId(sellerId)
			.userId(userId)
			.postNum(postNum)
			.build();

		roomEntity = RoomDto.toRoomEntity(roomDto);

		chatList = new ArrayList<>();
		chatList.add(
			ChatDto.builder()
				.roomId(roomId)
				.sender(sellerId)
				.message(message)
				.createdAt(createdAt)
				.build()
		);
		chatList.add(
			ChatDto.builder()
				.roomId(roomId)
				.sender(sellerId + 2)
				.message(message)
				.createdAt(createdAt)
				.build()
		);

		chatDto = ChatDto.builder()
			.roomId(roomId)
			.sender(sender)
			.message(message)
			.createdAt(createdAt)
			.build();

		chatCollection = ChatDto.toChatCollection(chatDto);

		chatCollectionList = new ArrayList<>();
		chatCollectionList.add(chatCollection);
		chatCollectionList.add(chatCollection);
	}

	@Test
	@DisplayName("채팅_방_조회_성공")
	void getRoomSuccess() {
		given(chatDao.findRoomId(roomDto.getUserId(), roomDto.getPostNum())).willReturn(roomEntity);

		ChatServiceImpl chatService = new ChatServiceImpl(chatDao);
		Assertions.assertNotNull(chatService.createRoom(roomDto));

		verify(chatDao).findRoomId(roomDto.getUserId(), roomDto.getPostNum());
	}

	@Test
	@DisplayName("채팅_방_생성_성공")
	void createRoomSuccess() {
		given(chatDao.findRoomId(roomDto.getUserId(), roomDto.getPostNum())).willReturn(null);
		given(chatDao.save(any(RoomEntity.class))).willReturn(roomEntity);

		ChatServiceImpl chatService = new ChatServiceImpl(chatDao);
		Assertions.assertNotNull(chatService.createRoom(roomDto));

		verify(chatDao).findRoomId(roomDto.getUserId(), roomDto.getPostNum());
		verify(chatDao).save(any(RoomEntity.class));
	}

	@Test
	@DisplayName("채팅_방_생성_실패")
	void createRoomFailure() {
		given(chatDao.findRoomId(roomDto.getUserId(), roomDto.getPostNum())).willReturn(null);
		given(chatDao.save(any(RoomEntity.class))).willReturn(null);

		ChatServiceImpl chatService = new ChatServiceImpl(chatDao);
		ChatException exception = Assertions.assertThrows(ChatException.class, () -> {
			chatService.createRoom(roomDto);
		});
		Assertions.assertEquals(exception.getMessage(), "채팅방을 생성하지 못했습니다.");

		verify(chatDao).findRoomId(roomDto.getUserId(), roomDto.getPostNum());
		verify(chatDao).save(any(RoomEntity.class));
	}

	@Test
	@DisplayName("채팅_저장_성공")
	void saveChatSuccess() {
		given(chatDao.save(any(ChatCollection.class))).willReturn(chatCollection);

		ChatServiceImpl chatService = new ChatServiceImpl(chatDao);
		chatService.saveChat(chatDto);

		verify(chatDao).save(any(ChatCollection.class));
	}

	@Test
	@DisplayName("채팅_저장_실패")
	void saveChatFailure() {
		given(chatDao.save(any(ChatCollection.class))).willReturn(null);

		ChatServiceImpl chatService = new ChatServiceImpl(chatDao);
		ChatException exception = Assertions.assertThrows(ChatException.class, () -> {
			chatService.saveChat(chatDto);
		});
		Assertions.assertEquals(exception.getMessage(), "채팅을 저장하지 못했습니다.");

		verify(chatDao).save(any(ChatCollection.class));
	}

	@Test
	@DisplayName("채팅_내역_조회_성공")
	void getChatListSuccess() {
		given(chatDao.findByRoomId(roomId)).willReturn(chatCollectionList);

		ChatServiceImpl chatService = new ChatServiceImpl(chatDao);
		Assertions.assertEquals(chatService.getChatList(roomId).size(), 2);

		verify(chatDao).findByRoomId(roomId);
	}

	@Test
	@DisplayName("채팅_내역_조회_실패")
	void getChatListFailure() {
		given(chatDao.findByRoomId(roomId)).willReturn(null);

		ChatServiceImpl chatService = new ChatServiceImpl(chatDao);
		ChatException exception = Assertions.assertThrows(ChatException.class, () -> {
			chatService.getChatList(roomId);
		});
		Assertions.assertEquals(exception.getMessage(), "채팅 내역 조회에 실패했습니다.");

		verify(chatDao).findByRoomId(roomId);
	}
}
