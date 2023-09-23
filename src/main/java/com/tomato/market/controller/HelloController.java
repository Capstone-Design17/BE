package com.tomato.market.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tomato.market.data.dto.HelloDto;
import com.tomato.market.service.HelloService;

@RestController
public class HelloController {

	private final Logger logger = LoggerFactory.getLogger(HelloController.class);

	private HelloService helloService;

	@Autowired
	public HelloController(HelloService helloService) {
		this.helloService = helloService;
	}

	@GetMapping("/hello")
	public String getHello() {

		logger.warn("HelloController is called");

		// DTO 생성
		HelloDto helloDto = new HelloDto("Hello", "world");

		// 저장 로직
		String save = helloService.saveHello(helloDto).toString();
		logger.info("save: " + save);

		// 조회 로직
		String get = helloService.getHello("Hello").toString();
		logger.info("get: " + get);
		return "Hello World!";
	}
}
