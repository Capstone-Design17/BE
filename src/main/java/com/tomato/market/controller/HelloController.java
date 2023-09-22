package com.tomato.market.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	private final Logger logger = LoggerFactory.getLogger(HelloController.class);

	@GetMapping("/hello")
	public String getHello() {
		logger.info("HelloController is called");
		logger.warn("HelloController is called");

		return "Hello  World!";
	}
}
