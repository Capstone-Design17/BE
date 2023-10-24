package com.tomato.market.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tomato.market.data.dto.ChatDto;
import com.tomato.market.data.dto.RoomDto;

@RestController("/api") // nginx proxy를 위해 필요
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

		https://klyhyeon.tistory.com/159?category=940721
	 */

	/* 필요 로직
		1. 채팅방 생성 : createRoom
		2. 채팅방 입장 : enterRoom, 1대1 채팅만 존재?
		3. 채팅방 메시지 송수신 : sendMessage
	 */


	@GetMapping("/room")
	public void createRoom(RoomDto roomDto) { // 채팅방 생성
		// chatRoomDto
		// PostNum, sellerId, userId로 chatRoomNum의 존재를 찾음
		// 있으면 Text File에서 대화 불러오기(List)
		// 없으면 새로 생성, 대화를 저장할 Text File 생성 : 상품아이디, 채팅방아이디 조합한 파일명
		// return으로 chatRoomNum을 포함한 ChatRoomDto 전달
	}

	@MessageMapping("/ws/") //{roomNum}
	@SendTo("/queue/{roomNum}")
	public void enterRoom(@Payload ChatDto chatDto) { // @Payload ChatDto chatDto
	}

	@MessageMapping("/chat.send.{roomId}") // '/app/chat'으로 온 Message Handle
	@SendTo("/topic/chat/{roomId}") // '/topic/chat/{roomId}'로 발송
	public ChatDto sendMessage(ChatDto chatDto, String roomId) throws ParseException {
		// 채팅방 Room 번호와 전송할 Message를 받음

		// Service에서 처리?
		// 1. Message를 받은 시간을 등록 : CurrentTimeStamp()
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date now = formatter.parse(String.valueOf(LocalDateTime.now()));
		chatDto.setCreatedAt(now);

		// 2. Message를 Text File에 저장

		// RoomNum으로 URL을 생성
		// URL로 Message 전송
		return chatDto; // @SendTo에 설정된 경로로 리턴
	}
}
