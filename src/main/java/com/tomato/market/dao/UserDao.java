package com.tomato.market.dao;

import com.tomato.market.data.entity.LocationEntity;
import com.tomato.market.data.entity.UserEntity;

public interface UserDao {
	public UserEntity save(UserEntity userEntity);

	public UserEntity get(String userId); // String id?

	public boolean existsByEmail(String userEmail);

	public boolean existsById(String id);

	public boolean existsByPhone(String phone);

	LocationEntity findByUserId(String userId);

	LocationEntity saveLocation(LocationEntity locationEntity);
}
