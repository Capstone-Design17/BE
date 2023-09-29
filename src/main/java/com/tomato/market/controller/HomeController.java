package com.tomato.market.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class HomeController {
	private final Logger logger = LoggerFactory.getLogger(HelloController.class);

	@GetMapping("/")
	public String getHome() {
		logger.info("HomeController.getHome() is called");

		return "Hello World!"; // home으로 redirect? or 애초에 react에서 home으로 링크?
	}
}
