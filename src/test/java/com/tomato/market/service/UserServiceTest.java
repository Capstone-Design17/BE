package com.tomato.market.service;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tomato.market.dao.impl.UserDaoImpl;
import com.tomato.market.data.dto.UserSignUpDto;
import com.tomato.market.data.entity.UserEntity;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	@Mock
	UserDaoImpl userDao;

	private String email = "test123@google.com";
	private String id = "test123";
	private String pwd = "test123!";
	private String realName = "testName";
	private String nickName = "11111111";
	private String phone = "01012345678";
	private Integer status = 0;
	private Date birthday;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


	private UserSignUpDto userSignUpDto;
	private UserEntity userEntity;

	@BeforeEach
	void setUp() throws ParseException {
		// DTO 받음
		birthday = formatter.parse("2023/09/27 01:25:27");
		userSignUpDto = UserSignUpDto.builder()
			.email(email)
			.id(id)
			.pwd(pwd)
			.realName(realName)
			.nickName(nickName)
			.phone(phone)
			.status(status)
			.birthday(birthday)
			.build();

		// Entity 변환
		userEntity = UserSignUpDto.toUserEntity(userSignUpDto);
	}

	@Test
	@DisplayName("회원가입_성공")
	void registerUserSuccess() {
		// Entity를 DAO에 전달, 중복 체크 성공한 상황 가정
		given(userDao.existsByEmail(userEntity.getEmail())).willReturn(true);
		given(userDao.existsById(userEntity.getId())).willReturn(true);
		given(userDao.existsByPhone(userEntity.getPhone())).willReturn(true);
		given(userDao.save(userEntity)).willReturn(userEntity);

		// 중복 체크
		Assertions.assertTrue(userDao.existsByEmail(userEntity.getEmail()));
		Assertions.assertTrue(userDao.existsById(userEntity.getId()));
		Assertions.assertTrue(userDao.existsByPhone(userEntity.getPhone()));
		Assertions.assertEquals(userDao.save(userEntity), userEntity);

		//
		verify(userDao).existsByEmail(userEntity.getEmail());
		verify(userDao).existsById(userEntity.getId());
		verify(userDao).existsByPhone(userEntity.getPhone());
		verify(userDao).save(userEntity);
	}

	@Test
	@DisplayName("회원가입_실패")
	void registerUserFail() {
		// Entity를 DAO에 전달, 중복 체크 성공한 상황 가정
		given(userDao.existsByEmail(userEntity.getEmail())).willReturn(true);
		given(userDao.existsById(userEntity.getId())).willReturn(true);
		given(userDao.existsByPhone(userEntity.getPhone())).willReturn(true);
		given(userDao.save(userEntity)).willReturn(null);

		// 중복 체크
		Assertions.assertTrue(userDao.existsByEmail(userEntity.getEmail()));
		Assertions.assertTrue(userDao.existsById(userEntity.getId()));
		Assertions.assertTrue(userDao.existsByPhone(userEntity.getPhone()));
		Assertions.assertNull(userDao.save(userEntity));

		//
		verify(userDao).existsByEmail(userEntity.getEmail());
		verify(userDao).existsById(userEntity.getId());
		verify(userDao).existsByPhone(userEntity.getPhone());
		verify(userDao).save(userEntity);
	}

	@Test
	@DisplayName("이메일_중복")
	void duplicatedEmail() {
		given(userDao.existsByEmail(userEntity.getEmail())).willReturn(false);

		Assertions.assertFalse(userDao.existsByEmail(userEntity.getEmail()));

		verify(userDao).existsByEmail(userEntity.getEmail());
	}

	@Test
	@DisplayName("아이디_중복")
	void duplicatedId() {
		given(userDao.existsByEmail(userEntity.getEmail())).willReturn(true);
		given(userDao.existsById(userEntity.getId())).willReturn(false);

		Assertions.assertTrue(userDao.existsByEmail(userEntity.getEmail()));
		Assertions.assertFalse(userDao.existsById(userEntity.getId()));

		verify(userDao).existsByEmail(userEntity.getEmail());
		verify(userDao).existsById(userEntity.getId());
	}

	@Test
	@DisplayName("전화번호_중복")
	void duplicatedPhone() {
		given(userDao.existsByEmail(userEntity.getEmail())).willReturn(true);
		given(userDao.existsById(userEntity.getId())).willReturn(true);
		given(userDao.existsByPhone(userEntity.getPhone())).willReturn(false);

		Assertions.assertTrue(userDao.existsByEmail(userEntity.getEmail()));
		Assertions.assertTrue(userDao.existsById(userEntity.getId()));
		Assertions.assertFalse(userDao.existsByPhone(userEntity.getPhone()));

		verify(userDao).existsByEmail(userEntity.getEmail());
		verify(userDao).existsById(userEntity.getId());
		verify(userDao).existsByPhone(userEntity.getPhone());
	}
}
