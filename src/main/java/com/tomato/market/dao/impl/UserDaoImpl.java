package com.tomato.market.dao.impl;


import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tomato.market.dao.UserDao;
import com.tomato.market.data.entity.UserEntity;
import com.tomato.market.data.repository.UserRepository;

@Service
public class UserDaoImpl implements UserDao {
	private final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

	private final UserRepository userRepository;

	public UserDaoImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserEntity save(UserEntity userEntity) {
		logger.info("UserDaoImpl.save() is called");

		// 데이터 저장
		UserEntity savedUserEntity = userRepository.save(userEntity);

		// 저장 실패 체크 // null 반환 말고 다른거로 반환?
		if (savedUserEntity.getEmail() != null) { // 저장 성공
			logger.info("UserDaoImpl.save() : 저장 성공");
			return savedUserEntity;
		} else { // 저장 실패
			logger.warn("UserDaoImpl.save() : 저장 실패");
			return null;
		}
	}

	@Override
	public UserEntity get(String id) {
		logger.info("UserDaoImpl.get() is called");

		// ID로 User 조회
		Optional<UserEntity> userEntity = userRepository.findById(id);

		if (userEntity.isPresent()) { // 객체가 존재하면
			logger.info("UserDaoImpl.get() : User 정보 조회 성공");
			return userEntity.get(); // Optional 내부의 객체를 반환
		} else {
			logger.warn("UserDaoImpl.get() : User 정보 조회 실패");
			return null;
		}
	}

	@Override
	public boolean existsByEmail(String userEmail) {
		logger.info("UserDaoImpl.existsByEmail() is called");
		boolean getUserEntity = userRepository.existsByEmail(userEmail);
		if (getUserEntity) { // 이메일 찾음
			logger.info("UserDaoImpl.existsByEmail() : 존재하는 이메일 찾음");
			return true;
		} else { // 이메일 못 찾음
			logger.info("UserDaoImpl.existsByEmail() : 존재하는 이메일 찾지 못함");
			return false;
		}
	}

	@Override
	public boolean existsById(String id) {
		logger.info("UserDaoImpl.existsById() is called");
		boolean getUserEntity = userRepository.existsById(id);
		if (getUserEntity) {
			logger.info("UserDaoImpl.existsById() : 존재하는 아이디 찾음");
			return true;
		} else {
			logger.info("UserDaoImpl.existsById() : 존재하는 아이디를 찾지 못함");
			return false;
		}
	}

	@Override
	public boolean existsByPhone(String phone) {
		logger.info("UserDaoImpl.existsByPhone() is called");
		boolean getUserEntity = userRepository.existsByPhone(phone);
		if (getUserEntity) {
			logger.info("UserDaoImpl.existsByPhone() : 존재하는 전화번호 찾음");
			return true;
		} else {
			logger.info("UserDaoImpl.existsByPhone() : 존재하는 전화번호를 찾지 못함");
			return false;
		}
	}
}
