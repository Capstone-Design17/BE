package com.tomato.market.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tomato.market.dao.UserDao;
import com.tomato.market.data.dto.UserSignUpDto;
import com.tomato.market.data.entity.UserEntity;
import com.tomato.market.handler.exception.UserException;
import com.tomato.market.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	private final UserDao userDao;

	@Autowired
	public UserServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public UserSignUpDto registerUser(UserSignUpDto userSignUpDto) {
		logger.info("UserServiceImpl.registerUser() is called");
		// DTO -> Entity 전환
		UserEntity userEntity = UserSignUpDto.toUserEntity(userSignUpDto);

		// 중복 체크
		if (!userDao.existsByEmail(userSignUpDto.getEmail())) { // 이메일 중복 체크
			logger.info("UserServiceImpl.registerUser() : 이메일 중복 체크 성공");
		} else { // 이메일 중복
			logger.warn("UserServiceImpl.registerUser() : 이메일 중복 체크 실패");
			throw new UserException("이미 가입된 이메일입니다.");
		}

		if (!userDao.existsById(userSignUpDto.getId())) {
			logger.info("UserServiceImpl.registerUser() : 아이디 중복 체크 성공");
		} else {
			logger.warn("UserServiceImpl.registerUser() : 아이디 중복 체크 실패");
			throw new UserException("이미 가입된 아이디입니다.");
		}

		if (!userDao.existsByPhone(userSignUpDto.getPhone())) {
			logger.info("UserServiceImpl.registerUser() : 전화번호 중복 체크 성공");
		} else {
			logger.warn("UserServiceImpl.registerUser() : 전화번호 중복 체크 실패");
			throw new UserException("이미 가입된 전화번호입니다.");
		}

		// 데이터 저장
		logger.info("UserServiceImpl.registerUser() : 데이터 저장 시도");
		UserEntity saveResult = userDao.save(userEntity);
		if (saveResult != null) { // 저장 성공
			logger.info("UserServiceImpl.registerUser() : 데이터 저장 성공");
			return UserSignUpDto.toUserSignUpDto(saveResult);
		} else { // 저장 실패
			logger.warn("UserServiceImpl.registerUser() : 데이터 저장 실패");
			throw new UserException("데이터 저장에 실패했습니다."); //
		}
	}

	@Override
	public UserSignUpDto loginUser(UserSignUpDto userSignUpDto) {
		return null;
	}
}
