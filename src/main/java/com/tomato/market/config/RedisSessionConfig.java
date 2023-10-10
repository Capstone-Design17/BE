package com.tomato.market.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession
public class RedisSessionConfig {
	private final Logger logger = LoggerFactory.getLogger(RedisSessionConfig.class);
	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private String port;

	@Value("${spring.redis.password}")
	private String password;

	@Bean
	@Profile("dev")
	public LettuceConnectionFactory redisConnectionFactory() {
		logger.info("RedisSessionConfig : Redis 연결");
		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
		lettuceConnectionFactory.setHostName(host);
		lettuceConnectionFactory.setPort(Integer.parseInt(port));
		lettuceConnectionFactory.setPassword(password);
		return lettuceConnectionFactory;
	}

	@Bean
	@Profile("local")
	public LettuceConnectionFactory redisConnectionFactory2() {
		logger.info("RedisSessionConfig : Redis 연결");
		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
		lettuceConnectionFactory.setHostName(host);
		lettuceConnectionFactory.setPort(Integer.parseInt(port));
		return lettuceConnectionFactory;
	}
}
