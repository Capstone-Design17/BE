package com.tomato.market.controller;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tomato.market.data.dto.ChatDto;
import com.tomato.market.data.dto.RoomDto;
import com.tomato.market.data.dto.RoomResponseDto;
import com.tomato.market.service.ChatService;

@RestController // nginx proxy를 위해 필요
public class ChatController {
	/* ChatRoomEntity
		채팅방의 정보를 저장
		송신자
		수신자
		게시글 번호
	*/
	/* ChatMessageEntity
		채팅 내역을 저장
		채팅방 번호 참조
	*/

	/* ChatHandler
		Server가 가공해야 하는 메시지 처리
		ex) Server에서 전송하는 이벤트 등
		@MessageMapping으로 대체
	 */

	/* flow
		1. Client가 Post에서 채팅하기 버튼을 누름 or 채팅 탭에서 특정 채팅방을 클릭
		2. 채팅방 생성 or 이미 있다면 불러오기?
		3. 채팅방에 접속
		4. 채팅 내역 불러오기
		5. 채팅 입력
		6. 채팅 저장 : text file에 저장?
	 */

	/* 필요 로직
		1. 채팅방 생성 : createRoom
		2. 채팅방 입장 : enterRoom, 1대1 채팅만 존재하면 필요 없음?
		3. 채팅 내역 불러오기 : getHistory
		4. 채팅방 메시지 송수신 : sendMessage
	 */

	private Logger logger = LoggerFactory.getLogger(ChatController.class);
	private ChatService chatService;
	private final SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	public ChatController(ChatService chatService, SimpMessagingTemplate simpMessagingTemplate) {
		this.chatService = chatService;
		this.simpMessagingTemplate = simpMessagingTemplate;
	}


	// 채팅하기 버튼을 눌렀을 때
	@PostMapping("/api/chat/room")
	public RoomResponseDto createRoom(@RequestBody RoomDto roomDto) { // 채팅방 생성
		logger.info("ChatController.createRoom() is called");
		// PostNum, userId로 기존의 Room의 존재를 찾음
		RoomDto room = chatService.createRoom(roomDto);

		// RoomNum이 포함된 RoomDto 반환 // roomId만 반환할수도
		logger.info("ChatController.createRoom() : roomId 반환");
		return RoomResponseDto.builder()
			.status(HttpStatus.OK)
			.message("채팅방 불러오기 성공")
			.roomDto(room)
			.build();
	}

	// 채팅방 입장 시 채팅 내역 불러오기
//	@GetMapping("/api/chat/room")
//	public ChatListResponseDto getChatList(String roomId) {
//		logger.info("ChatController.getChatLIst() is called");
//
//		// RoomId로 MongoDB에서 채팅 내력 리스트 반환
//		logger.info("ChatController.getChatLIst() : ChatList 호출");
//		chatService.getChatList(roomId);
//
//		// Response 객체에 담아 반환
//		return ChatListResponseDto.builder()
//			.chatList(chatList)
//			.build();
//	}

	// 채팅 테스트용 메소드
	@MessageMapping("/hello")
	@SendTo("/topic/hello")
	public ChatDto sendMessage(ChatDto chatDto) {
		logger.info("ChatController.sendMessage() is called");
		logger.info("ChatController.sendMessage() : " + chatDto.toString());
		// 클라이언트로부터 메시지를 받아 처리한 후 응답을 반환
		String content = "Received: " + chatDto.getMessage();
		return ChatDto.builder().message(content).build();
	}

	@MessageMapping("/chat")
	public void sendMessage(ChatDto chatDto, String roomId) throws ParseException {
		logger.info("ChatController.sendMessage() is called");
		logger.info("ChatController.sendMessage() : room" + chatDto.getRoomId() + "로 송신");
		// 채팅방 Room 번호와 전송할 Message를 받음

		// Service에서 처리?
		// 1. Message를 받은 시간을 등록
		logger.info("ChatController.sendMessage() : 현재 시간 등록");
		LocalDateTime now = LocalDateTime.now();
		String date = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
		chatDto.setCreatedAt(date);

		// 2. Message를 MongoDb에 저장
		logger.info("ChatController.sendMessage() : 데이터 저장 시도");
		chatService.saveChat(chatDto);

		// RoomNum으로 URL을 생성
		// URL로 Message 전송
		logger.info("ChatController.sendMessage() : 채팅 전송");
		simpMessagingTemplate.convertAndSend("/topic/chat/" + chatDto.getRoomId(), chatDto);
	}
}
