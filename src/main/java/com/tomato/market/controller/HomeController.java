package com.tomato.market.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	@GetMapping("/")
	public String getHome() {
		return "Hello World!"; // home으로 redirect? or 애초에 react에서 home으로 링크?
	}
}
