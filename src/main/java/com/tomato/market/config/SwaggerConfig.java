package com.tomato.market.config;

import java.lang.reflect.Type;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;


@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openApi() {
		Info info = new Info().version("v1.0.0").title("Swagger를 이용한 API 테스트").description("");

		return new OpenAPI().info(info);
	}

	// swagger에 multipart/form-data 형식을 인지시키기 위한 설정
	@Component
	public class MultipartJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {

		/**
		 * "Content-Type: multipart/form-data" 헤더를 지원하는 HTTP 요청 변환기
		 */
		public MultipartJackson2HttpMessageConverter(ObjectMapper objectMapper) {
			super(objectMapper, MediaType.APPLICATION_OCTET_STREAM);
		}

		@Override
		public boolean canWrite(Class<?> clazz, MediaType mediaType) {
			return false;
		}

		@Override
		public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
			return false;
		}

		@Override
		protected boolean canWrite(MediaType mediaType) {
			return false;
		}
	}

}
