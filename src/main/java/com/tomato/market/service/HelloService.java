package com.tomato.market.service;

import com.tomato.market.data.dto.HelloDto;

public interface HelloService {
	HelloDto saveHello(HelloDto helloDto);

	HelloDto getHello(String helloId);
}
