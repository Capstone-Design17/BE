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

		// DAO 호출
		if (!userDao.existsByEmail(userSignUpDto.getEmail())) { // 이메일 중복 체크
			logger.info("UserServiceImpl.registerUser() : 이메일 중복 체크 성공");
			// 닉네임 중복 체크
			// 전화번호 중복 체크 // and로 중복 체크 묶기? // DAO로 예외처리 위임 불가능 -> DAO는 Register에 국한되지 않음
		} else { // 이메일 중복
			logger.warn("UserServiceImpl.registerUser() : 이메일 중복 체크 실패");
			throw new UserException("중복된 이메일 입니다."); // 에러 리턴?
		}


		logger.info("UserServiceImpl.registerUser() : 데이터 저장 시도");
		UserEntity saveResult = userDao.save(userEntity);
		if (saveResult != null) { // 저장 성공
			logger.info("UserServiceImpl.registerUser() : 데이터 저장 성공");
			// 저장 성공 결과 반환
			return UserSignUpDto.toUserSignUpDto(saveResult); // 저장 성공을 어떻게 나타낼까
		} else { // 저장 실패
			logger.warn("UserServiceImpl.registerUser() : 데이터 저장 실패");
			throw new UserException("데이터 저장에 실패했습니다."); // 에러 리턴?
		}
	}

	@Override
	public UserSignUpDto loginUser(UserSignUpDto userSignUpDto) {
		return null;
	}
}
