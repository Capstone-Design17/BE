package com.tomato.market.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;


@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openApi() {
		Info info = new Info().version("v1.0.0").title("Swagger를 이용한 API 테스트").description("");

		return new OpenAPI().info(info);
	}

}
