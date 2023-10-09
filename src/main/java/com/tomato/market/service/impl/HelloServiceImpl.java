package com.tomato.market.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomato.market.dao.HelloDao;
import com.tomato.market.data.dto.HelloDto;
import com.tomato.market.data.entity.HelloEntity;
import com.tomato.market.service.HelloService;

@Service
public class HelloServiceImpl implements HelloService {

	HelloDao helloDao;

	@Autowired
	public HelloServiceImpl(HelloDao helloDao) {
		this.helloDao = helloDao;
	}

	@Override
	public HelloDto saveHello(HelloDto helloDto) {
		// DTO -> Entity 변환
		HelloEntity helloEntity = new HelloEntity(helloDto.getId(), helloDto.getText());
		helloDao.saveHelloEntity(helloEntity);

		// 변환 잘 되었는지 확인
		HelloDto helloDto2 = new HelloDto(helloEntity.getId(), helloEntity.getText());
		return helloDto2;
	}

	@Override
	public HelloDto getHello(String helloId) {
		HelloEntity helloEntity = helloDao.getHelloEntity(helloId);

		// Entity -> DTO 변환
		HelloDto helloDto = new HelloDto(helloEntity.getId(), helloEntity.getText());
		return helloDto;
	}
}
