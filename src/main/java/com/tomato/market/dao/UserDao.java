package com.tomato.market.dao;

import com.tomato.market.data.entity.UserEntity;

public interface UserDao {
	public UserEntity save(UserEntity userEntity);

	public UserEntity get(UserEntity userEntity); // String id?
}
