package com.tomato.market.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomato.market.dao.HelloDao;
import com.tomato.market.data.entity.HelloEntity;
import com.tomato.market.data.repository.HelloRepository;

@Service
public class HelloDaoImpl implements HelloDao {

	HelloRepository helloRepository;

	@Autowired
	public HelloDaoImpl(HelloRepository helloRepository) {
		this.helloRepository = helloRepository;
	}

	@Override
	public HelloEntity saveHelloEntity(HelloEntity helloEntity) {
		helloRepository.save(helloEntity);
		return helloEntity;
	}

	@Override
	public HelloEntity getHelloEntity(String helloId) {
		HelloEntity helloEntity = helloRepository.getById(helloId);
		return helloEntity;
	}
}
