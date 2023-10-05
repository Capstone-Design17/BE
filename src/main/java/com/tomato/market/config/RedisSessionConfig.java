package com.tomato.market.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession
public class RedisSessionConfig {
	private final Logger logger = LoggerFactory.getLogger(RedisSessionConfig.class);

	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {
		logger.info("RedisSessionConfig : Redis 연결");
		return new LettuceConnectionFactory();
	}
}
