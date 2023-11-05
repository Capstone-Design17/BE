package com.tomato.market.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomato.market.data.dto.ChatDto;
import com.tomato.market.data.dto.ImageDto;
import com.tomato.market.data.dto.RoomDto;
import com.tomato.market.handler.exception.ChatException;
import com.tomato.market.service.impl.BoardServiceImpl;
import com.tomato.market.service.impl.ChatServiceImpl;

@WebMvcTest(ChatController.class)
public class ChatControllerTest {
	@Autowired
	private MockMvc mockmvc;
	@MockBean
	private ChatServiceImpl chatService;
	@MockBean
	private BoardServiceImpl boardService;
	@MockBean
	private SimpMessagingTemplate simpMessagingTemplate;
	@Autowired
	private WebApplicationContext ctx;

	// Room
	private String roomId = "uuid-seller-buyer";
	private String sellerId = "seller";
	private String userId = "buyer";
	private Integer postNum = 50;
	private RoomDto roomDto;

	// ChatList
	private List<ChatDto> chatList;
	private String message = "Hello World!";
	private String createdAt = "2023/11/10 01:03:08";

	// Chat
	private ChatDto chatDto;
	private String sender = "sender";

	// Content
	private String content;

	// RoomList
	private List<RoomDto> roomList;

	// Image
	private ImageDto imageDto;
	private String imageName = "original.png";
	private String uuid = "uuidoriginal.png";

	// ImageList
	private List<ImageDto> imageList;

	@BeforeEach
	void setUp() {
		mockmvc = MockMvcBuilders.webAppContextSetup(ctx)
			.addFilters(new CharacterEncodingFilter("UTF-8", true)).build();


		roomDto = RoomDto.builder()
			.roomId(roomId)
			.sellerId(sellerId)
			.userId(userId)
			.postNum(postNum)
			.build();

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
			.build();


		roomList = new ArrayList<>();
		roomList.add(roomDto);
		roomList.add(roomDto);

		imageDto = ImageDto.builder()
			.postNum(postNum)
			.imageName(imageName)
			.uuid(uuid)
			.build();

		imageList = new ArrayList<>();
		imageList.add(imageDto);
		imageList.add(imageDto);
	}

	@Test
	@DisplayName("채팅_방_생성_성공")
	void createRoomSuccess() throws Exception {
		// RoomDto로 채팅방을 조회, 성공
		// 없으면 Service 단에서 생성해서 돌려줌
		given(chatService.createRoom(any(RoomDto.class))).willReturn(roomDto);

		content = new ObjectMapper().writeValueAsString(roomDto);

		mockmvc.perform(post("/api/chat/room")
				.contentType(MediaType.APPLICATION_JSON)
				.content(content)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.roomDto.roomId", is(roomId)))
			.andExpect(jsonPath("$.message", is("채팅방 불러오기 성공")))
			.andDo(print());

		verify(chatService).createRoom(any(RoomDto.class));
	}

	@Test
	@DisplayName("채팅_방_생성_실패")
	void createRoomFailure() throws Exception {
		// Validation을 지키지 못한 상황

		// PostNum을 넘겨받지 못한 상황을 가정
		roomDto.setSellerId(null);
		content = new ObjectMapper().writeValueAsString(roomDto);

		mockmvc.perform(post("/api/chat/room")
				.contentType(MediaType.APPLICATION_JSON)
				.content(content)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message.sellerId", is("SellerId는 필수 입력값입니다.")))
			.andDo(print());
	}

	@Test
	@DisplayName("채팅_내역_불러오기_성공")
	void getChatListSuccess() throws Exception {
		// Document 형식
		given(chatService.getChatList(roomId)).willReturn(chatList);

		mockmvc.perform(get("/api/chat/room")
				.param("roomId", roomId)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message", is("채팅 내역 조회 성공")))
			.andExpect(jsonPath("$.chatList").exists())
			.andDo(print());

		verify(chatService).getChatList(roomId);
	}

	@Test
	@DisplayName("채팅_내역_불러오기_실패")
	void getChatListFailure() throws Exception {
		given(chatService.getChatList(roomId)).willThrow(new ChatException("채팅 내역 조회에 실패했습니다."));

		mockmvc.perform(get("/api/chat/room").param("roomId", roomId)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message", is("채팅 내역 조회에 실패했습니다.")))
			.andDo(print());

		verify(chatService).getChatList(roomId);
	}

	@Test
	@DisplayName("채팅_전송_성공")
	void sendMessageSuccess() throws ParseException {
		// 채팅 데이터 저장 성공 포함
		doNothing().when(chatService).saveChat(chatDto);

		// 전송 실패는 없음, 원하지 않은 곳으로 전송될 뿐임
		doNothing().when(simpMessagingTemplate).convertAndSend(any(String.class), any(ChatDto.class));

		ChatController chatController = new ChatController(chatService, boardService, simpMessagingTemplate);
		chatController.sendMessage(chatDto, roomId);

		verify(chatService).saveChat(chatDto);
		verify(simpMessagingTemplate).convertAndSend("/topic/chat/" + chatDto.getRoomId(), chatDto);
	}

	@Test
	@DisplayName("채팅_전송_데이터_저장_실패")
	void saveMessageFailure() throws ParseException {
		doThrow(new ChatException("채팅을 저장하지 못했습니다.")).when(chatService).saveChat(chatDto);

		ChatController chatController = new ChatController(chatService, boardService, simpMessagingTemplate);
		ChatException exception = Assertions.assertThrows(ChatException.class, () -> {
			chatController.sendMessage(chatDto, roomId);
		});
		Assertions.assertEquals(exception.getMessage(), "채팅을 저장하지 못했습니다.");

		verify(chatService).saveChat(chatDto);
	}

	@Test
	@DisplayName("채팅_방_목록_조회_성공")
	void getRoomListSuccess() throws Exception {
		given(chatService.getRoomList(any(String.class))).willReturn(roomList);
		given(boardService.getPostImage(any(Integer.class))).willReturn(imageDto);

		mockmvc.perform(get("/api/chat/list").param("userId", userId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message", is("채팅 목록 조회 성공")))
			.andExpect(jsonPath("$.roomList").exists())
			.andExpect(jsonPath("$.imageList").exists())
			.andDo(print());

		verify(chatService).getRoomList(any(String.class));
		verify(boardService, times(2)).getPostImage(any(Integer.class));
	}

	@Test
	@DisplayName("채팅_방_목록_조회_실패")
	void getRoomListFailure() throws Exception {
		given(chatService.getRoomList(any(String.class))).willThrow(new ChatException("채팅 목록 조회에 실패했습니다."));

		mockmvc.perform(get("/api/chat/list").param("userId", userId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message", is("채팅 목록 조회에 실패했습니다.")))
			.andDo(print());

		verify(chatService).getRoomList(any(String.class));
	}
}
