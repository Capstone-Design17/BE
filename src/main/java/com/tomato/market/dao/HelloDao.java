package com.tomato.market.dao;

import com.tomato.market.data.entity.HelloEntity;

public interface HelloDao {
	HelloEntity saveHelloEntity(HelloEntity helloEntity);

	HelloEntity getHelloEntity(String id);
}
