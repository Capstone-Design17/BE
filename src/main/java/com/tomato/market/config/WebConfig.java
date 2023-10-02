package com.tomato.market.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	// CORS 처리


	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")    // CORS를 적용할 URL 패턴
			.allowedOrigins("http://localhost:3000", "http://localhost:80")    // 허용할 출처
			.allowedMethods("*");        // HTTP 메소드 제한
	}
}
