package com.tomato.market.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.tomato.market.dao.UserDao;
import com.tomato.market.data.entity.UserEntity;
import com.tomato.market.data.repository.UserRepository;

public class UserDaoImpl implements UserDao {
	@Autowired
	UserRepository userRepository;

	@Override
	public UserEntity save(UserEntity userEntity) {
//		Optional<UserEntity> saveResult = userRepository.save(userEntity); // 추후 반영
		UserEntity saveResult = userRepository.save(userEntity);

		// 중복 체크 등 필요?

		if (saveResult != null) { // DB에 저장 성공

		} else { // DB에 저장 실패

		}
		return saveResult;
	}

	@Override
	public UserEntity get(UserEntity userEntity) {
		return null;
	}
}
