package com.tomato.market.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.tomato.market.dao.UserDao;
import com.tomato.market.data.dto.UserDto;
import com.tomato.market.data.entity.UserEntity;
import com.tomato.market.service.UserService;

public class UserServiceImpl implements UserService {
	@Autowired
	UserDao userDao;

	@Override
	public UserDto registerUser(UserDto userDto) {
		// DTO -> Entity 전환
		UserEntity userEntity = UserEntity.builder().build();

		// DAO 호출
		UserEntity saveResult = userDao.save(userEntity);
		if (saveResult != null) { // 저장 성공
			// 저장 성공 결과 반환
			return UserDto.builder().build();
		} else { // 저장 실패
			return null;
		}
	}

	@Override
	public UserDto loginUser(UserDto userDto) {
		return null;
	}
}
