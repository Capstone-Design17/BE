package com.tomato.market.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tomato.market.dao.UserDao;
import com.tomato.market.data.dto.UserLoginDto;
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
	public UserLoginDto loginUser(UserLoginDto userLoginDto) {
		logger.info("UserServiceImpl.loginUser() is called");
		// DTO -> Entity 변환 : 필요 없을지도?

		// DAO에서 ID로 유저 정보 조회
		UserEntity userEntity = userDao.get(userLoginDto.getId());
		// 아이디로 객체 찾음
		if (userEntity != null) { // 사용자 정보 찾음
			logger.info("UserServiceImpl.loginUser() : 등록된 사용자 정보를 찾음");
			// 비밀번호 일치 확인
			if (userEntity.getPwd().equals(userLoginDto.getPwd())) { // 비밀번호 일치
				logger.info("UserServiceImpl.loginUser() : 비밀번호 일치 확인");
				return UserLoginDto.toUserLoginDto(userEntity); // Entity를 DTO로 변환시켜 반환
			} else { // 비밀번호 불일치
				logger.warn("UserServiceImpl.loginUser() : 비밀번호 불일치");
				throw new UserException("비밀번호가 일치하지 않습니다.");
			}

		} else {
			logger.warn("UserServiceImpl.loginUser() : 등록된 사용자 정보를 찾지 못함");
			throw new UserException("등록되지 않은 아이디입니다.");
		}
	}
}
