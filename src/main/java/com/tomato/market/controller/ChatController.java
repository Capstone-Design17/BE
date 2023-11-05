package com.tomato.market.controller;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomato.market.data.dto.ChatDto;
import com.tomato.market.data.dto.ChatListResponseDto;
import com.tomato.market.data.dto.ImageDto;
import com.tomato.market.data.dto.RoomDto;
import com.tomato.market.data.dto.RoomListResponseDto;
import com.tomato.market.data.dto.RoomResponseDto;
import com.tomato.market.service.BoardService;
import com.tomato.market.service.ChatService;

import jakarta.validation.Valid;

@RestController // nginx proxy를 위해 필요
public class ChatController {
	/* RoomEntity
		채팅방의 정보를 저장
		채팅방 아이디
		송신자
		수신자
		게시글 번호
	*/

	/* ChatCollection
		채팅 내역을 저장
		채팅방 아이디 참조
	*/

	/* flow
		1. Client가 Post에서 채팅하기 버튼을 누름 or 채팅 탭에서 특정 채팅방을 클릭
		2. 채팅방 생성 or 이미 있다면 불러오기
		3. 채팅방에 접속 : WebSocket
		4. 채팅 내역 불러오기
		5. 채팅 입력
		6. 채팅 저장 : MongoDB에 저장
	 */

	/* 필요 로직
		1. 채팅방 생성 : createRoom
		2. 채팅 내역 불러오기 : getChatList
		3. 채팅방 메시지 송수신 : sendMessage
	 */

	private Logger logger = LoggerFactory.getLogger(ChatController.class);
	private ChatService chatService;

	// PostImage를 불러오기 위해 BoardService의 코드를 가져다 씀, 옳은 방식인지는 모르겠다
	private BoardService boardService;
	private final SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	public ChatController(
		ChatService chatService, BoardService boardService, SimpMessagingTemplate simpMessagingTemplate) {
		this.chatService = chatService;
		this.boardService = boardService;
		this.simpMessagingTemplate = simpMessagingTemplate;
	}

	// 채팅하기 버튼을 눌렀을 때
	@PostMapping("/api/chat/room")
	public RoomResponseDto createRoom(@RequestBody @Valid RoomDto roomDto) { // 채팅방 생성 // Validation?
		logger.info("ChatController.createRoom() is called");
		logger.info("ChatController.createRoom() : " + roomDto.toString());
		// PostNum, userId로 기존의 Room의 존재를 찾음
		RoomDto room = chatService.createRoom(roomDto);
		logger.info("ChatController.createRoom() : " + room);

		// RoomNum이 포함된 RoomDto 반환 // roomId만 반환할수도
		logger.info("ChatController.createRoom() : roomId 반환");
		return RoomResponseDto.builder()
			.status(HttpStatus.OK)
			.message("채팅방 불러오기 성공")
			.roomDto(room)
			.build();
	}

	// 채팅방 입장 시 채팅 내역 불러오기
	@GetMapping("/api/chat/room")
	public ChatListResponseDto getChatList(@RequestParam String roomId) {
		logger.info("ChatController.getChatLIst() is called");

		// RoomId로 MongoDB에서 채팅 내력 리스트 반환
		logger.info("ChatController.getChatLIst() : ChatList 호출");
		List<ChatDto> chatList = chatService.getChatList(roomId);

		logger.info(chatList.toString());

		// Response 객체에 담아 반환
		return ChatListResponseDto.builder()
			.status(HttpStatus.OK)
			.message("채팅 내역 조회 성공")
			.chatList(chatList)
			.build();
	}

	@MessageMapping("/chat")
	public void sendMessage(ChatDto chatDto, String roomId) throws ParseException {
		logger.info("ChatController.sendMessage() is called");
		logger.info("ChatController.sendMessage() : room" + chatDto.getRoomId() + "로 송신");
		// 채팅방 Room 번호와 전송할 Message를 받음

		// 1. Message를 받은 시간을 등록
		logger.info("ChatController.sendMessage() : 현재 시간 등록");
		LocalDateTime now = LocalDateTime.now();
		String date = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
		chatDto.setCreatedAt(date);

		// 2. Message를 MongoDB에 저장
		logger.info("ChatController.sendMessage() : 데이터 저장 시도");
		chatService.saveChat(chatDto);

		// RoomId로 URL을 생성
		// URL로 Message 전송
		logger.info("ChatController.sendMessage() : 채팅 전송");
		simpMessagingTemplate.convertAndSend("/topic/chat/" + chatDto.getRoomId(), chatDto);
	}

	// 채팅방 내역 불러오기
	@GetMapping("/api/chat/list")
	public RoomListResponseDto getRoomList(String userId) {
		logger.info("ChatController.getRoomList() is called");
		// 내가 판매중인, 구매중인 채팅 내역을 모두 가져와야 함
		// RoomEntity의 SellerId, UserId를 모두 검색 -> JOIN?, 최신(역순)으로 출력
		// FRONT에서 SellerId와 UserId가 같으면 Disabled 처리 해뒀음

		// 채팅 목록 조회
		List<RoomDto> roomList = chatService.getRoomList(userId);
		logger.info("ChatController.getRoomList() : 채팅 목록 조회 성공");
		// 각 채팅의 썸네일 이미지 조회


		// BoardService 가져다 쓰기?
		// 찾은 RoomList에서 각 Post의 ID로 Image를 찾음
		List<ImageDto> imageList = new ArrayList<>();
		for (RoomDto roomDto : roomList) {
			// 썸네일로 사용할 Image 1개만 필요
			imageList.add(boardService.getPostImage(roomDto.getPostNum()));
		}
		logger.info("BoardController.getPostList() : 게시글의 이미지 정보를 찾음");

		return RoomListResponseDto.builder()
			.status(HttpStatus.OK)
			.message("채팅 목록 조회 성공")
			.roomList(roomList)
			.imageList(imageList)
			.build();
	}
}
