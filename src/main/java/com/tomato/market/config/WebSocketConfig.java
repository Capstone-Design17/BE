package com.tomato.market.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer { // STOMP가 탑재된 WebSocket
	private final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);


	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		logger.info("WebSocketConfig.registerStompEndPoints() is called");
		// 소켓 연결 관련 설정
		registry.addEndpoint("/api/ws") // WebSocket 서버에 접속하는 url
			.setAllowedOriginPatterns("*"); // CORS
//			.withSockJS(); // SockJS // Client에서 WebSocket으로 연결하는 경우 400에러의 원인
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		logger.info("WebSocketConfig.configureMessageBroker() is called");
		// 스프링이 제공하는 내장 브로커 사용
		// prefix로 subscribe한 유저에게 다이렉트로 메시지 전달
		registry.enableSimpleBroker("/queue", "/topic"); // queue : 1대1, topic : broadcast, subscribe
		// @MessageMapping으로 라우팅해서 Controller에서 처리할 Destination
		registry.setApplicationDestinationPrefixes("/app"); // pub
	}
}
