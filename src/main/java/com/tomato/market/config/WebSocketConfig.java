package com.tomato.market.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer { // STOMP가 탑재된 WebSocket


	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// 소켓 연결 관련 설정
		registry.addEndpoint("/ws/chat") // Client에 접속하는 url
			.setAllowedOrigins("/") // CORS
			.withSockJS(); // SockJS
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// 스프링이 제공하는 내장 브로커 사용
		registry.enableSimpleBroker("/queue", "/topic"); // queue : 1대1, topic : broadcast

	}
}
