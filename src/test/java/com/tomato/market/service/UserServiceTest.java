package com.tomato.market.service;

import static org.mockito.ArgumentMatchers.any;
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
import com.tomato.market.data.dto.UserLoginDto;
import com.tomato.market.data.dto.UserSignUpDto;
import com.tomato.market.data.entity.LocationEntity;
import com.tomato.market.data.entity.UserEntity;
import com.tomato.market.handler.exception.UserException;
import com.tomato.market.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	@Mock
	UserDaoImpl userDao;

	private String email = "daejin@daejin.ac.kr";
	private String id = "spring";
	private String pwd = "spring@123";
	private String name = "홍길동";
	private String nickName = "nick";
	private String phone = "01012345678";
	private Integer status = 0;
	private Date birth;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


	private UserSignUpDto userSignUpDto;
	private UserEntity userEntity;
	private UserLoginDto userLoginDto;

	private LocationEntity locationEntity;
	private String location = "서울시 ...";

	@BeforeEach
	void setUp() throws ParseException {
		// DTO 받음
		birth = formatter.parse("2023/09/27 01:25:27");

		// 회원가입용 DTO
		userSignUpDto = UserSignUpDto.builder()
			.email(email)
			.id(id)
			.pwd(pwd)
			.name(name)
			.nickName(nickName)
			.phone(phone)
			.status(status)
			.birth(birth)
			.build();

		// Entity 변환
		userEntity = UserSignUpDto.toUserEntity(userSignUpDto);

		// 로그인용 DTO
		userLoginDto = UserLoginDto.builder()
			.id(id)
			.pwd(pwd)
			.build();

		locationEntity = LocationEntity.builder()
			.userId(id)
			.location(location)
			.build();
	}

	@Test
	@DisplayName("회원가입_성공")
	void registerUserSuccess() {
		// Entity를 DAO에 전달, 중복 체크 성공한 상황 가정
		given(userDao.existsByEmail(userEntity.getEmail())).willReturn(false);
		given(userDao.existsById(userEntity.getId())).willReturn(false);
		given(userDao.existsByPhone(userEntity.getPhone())).willReturn(false);
		given(userDao.save(any(UserEntity.class))).willReturn(userEntity);

		UserServiceImpl userService = new UserServiceImpl(userDao);
		// 중복 체크 후 UserEntity -> UserSignUpDto로 변환하여 리턴할 것
		Assertions.assertEquals(userService.registerUser(userSignUpDto), UserSignUpDto.toUserSignUpDto(userEntity));

		//
		verify(userDao).existsByEmail(userEntity.getEmail());
		verify(userDao).existsById(userEntity.getId());
		verify(userDao).existsByPhone(userEntity.getPhone());
		verify(userDao).save(any(UserEntity.class));
	}

	@Test
	@DisplayName("회원가입_데이터_저장_실패")
	void registerUserFail() {
		// Entity를 DAO에 전달, 중복 체크 성공한 상황 가정
		given(userDao.existsByEmail(userEntity.getEmail())).willReturn(false);
		given(userDao.existsById(userEntity.getId())).willReturn(false);
		given(userDao.existsByPhone(userEntity.getPhone())).willReturn(false);
		given(userDao.save(any(UserEntity.class))).willReturn(null);

		// 여기서부터
		UserServiceImpl userService = new UserServiceImpl(userDao);

		// 예외를 던지는지 확인
		UserException exception = Assertions.assertThrows(UserException.class, () -> {
			userService.registerUser(userSignUpDto);
		});
		Assertions.assertEquals(exception.getMessage(), "데이터 저장에 실패했습니다.");

		//
		verify(userDao).existsByEmail(userEntity.getEmail());
		verify(userDao).existsById(userEntity.getId());
		verify(userDao).existsByPhone(userEntity.getPhone());
		verify(userDao).save(any(UserEntity.class));
	}

	@Test
	@DisplayName("회원가입_이메일_중복")
	void duplicatedEmail() {
		given(userDao.existsByEmail(userEntity.getEmail())).willReturn(true);

		UserServiceImpl userService = new UserServiceImpl(userDao);
		UserException exception = Assertions.assertThrows(UserException.class, () -> {
			userService.registerUser(userSignUpDto);
		});
		Assertions.assertEquals(exception.getMessage(), "이미 가입된 이메일입니다.");

		verify(userDao).existsByEmail(userEntity.getEmail());
	}

	@Test
	@DisplayName("회원가입_아이디_중복")
	void duplicatedId() {
		given(userDao.existsByEmail(userEntity.getEmail())).willReturn(false);
		given(userDao.existsById(userEntity.getId())).willReturn(true);

		UserServiceImpl userService = new UserServiceImpl(userDao);
		UserException exception = Assertions.assertThrows(UserException.class, () -> {
			userService.registerUser(userSignUpDto);
		});
		Assertions.assertEquals(exception.getMessage(), "이미 가입된 아이디입니다.");

		verify(userDao).existsByEmail(userEntity.getEmail());
		verify(userDao).existsById(userEntity.getId());
	}

	@Test
	@DisplayName("회원가입_전화번호_중복")
	void duplicatedPhone() {
		given(userDao.existsByEmail(userEntity.getEmail())).willReturn(false);
		given(userDao.existsById(userEntity.getId())).willReturn(false);
		given(userDao.existsByPhone(userEntity.getPhone())).willReturn(true);

		UserServiceImpl userService = new UserServiceImpl(userDao);
		UserException exception = Assertions.assertThrows(UserException.class, () -> {
			userService.registerUser(userSignUpDto);
		});
		Assertions.assertEquals(exception.getMessage(), "이미 가입된 전화번호입니다.");

		verify(userDao).existsByEmail(userEntity.getEmail());
		verify(userDao).existsById(userEntity.getId());
		verify(userDao).existsByPhone(userEntity.getPhone());
	}

	@Test
	@DisplayName("로그인_성공")
	void loginUserSuccess() {
		// 확안용 UserEntity 사용
		given(userDao.get(userLoginDto.getId())).willReturn(userEntity);

		UserServiceImpl userService = new UserServiceImpl(userDao);
		UserLoginDto result = userService.loginUser(userLoginDto);

		// id로 있는 User인지 조회, 비밀번호 일치 확인
		Assertions.assertEquals(result, UserLoginDto.toUserLoginDto(userEntity));

		verify(userDao).get(userLoginDto.getId());
	}


	@Test
	@DisplayName("로그인_아이디_조회_실패")
	void loginUserIdFail() {
		given(userDao.get(userLoginDto.getId())).willReturn(null);

		UserServiceImpl userService = new UserServiceImpl(userDao);
		UserException exception = Assertions.assertThrows(UserException.class, () -> {
			userService.loginUser(userLoginDto);
		});
		Assertions.assertEquals(exception.getMessage(), "등록되지 않은 아이디입니다.");

		verify(userDao).get(userLoginDto.getId());
	}

	@Test
	@DisplayName("로그인_비밀번호_확인_실패")
	void loginUserPwdFail() {
		// 아이디가 저장된 상황 가정
		given(userDao.get(userLoginDto.getId())).willReturn(userEntity);

		// 비밀번호 불일치 가정
		userLoginDto.setPwd(userLoginDto.getPwd() + 1);

		// 행위
		UserServiceImpl userService = new UserServiceImpl(userDao);
		UserException exception = Assertions.assertThrows(UserException.class, () -> {
			userService.loginUser(userLoginDto);
		});
		Assertions.assertEquals(exception.getMessage(), "비밀번호가 일치하지 않습니다.");

		// 검증
		verify(userDao).get(userLoginDto.getId());
	}

	@Test
	@DisplayName("사용자_닉네임_변경_성공")
	void updateNicknameSuccess() {
		given(userDao.get(any(String.class))).willReturn(userEntity);
		given(userDao.save(any(UserEntity.class))).willReturn(userEntity);

		UserServiceImpl userService = new UserServiceImpl(userDao);
		Assertions.assertEquals(userService.updateNickname(id, nickName), nickName);

		verify(userDao).get(any(String.class));
		verify(userDao).save(any(UserEntity.class));
	}

	@Test
	@DisplayName("사용자_닉네임_변경_실패")
	void updateNicknameFailure() {
		given(userDao.get(any(String.class))).willReturn(userEntity);
		given(userDao.save(any(UserEntity.class))).willReturn(null);

		UserServiceImpl userService = new UserServiceImpl(userDao);
		UserException exception = Assertions.assertThrows(UserException.class, () -> {
			userService.updateNickname(id, nickName);
		});
		Assertions.assertEquals(exception.getMessage(), "닉네임 변경에 실패했습니다.");

		verify(userDao).get(any(String.class));
		verify(userDao).save(any(UserEntity.class));
	}

	@Test
	@DisplayName("사용자_비밀번호_변경_성공")
	void updatePasswordSuccess() {
		given(userDao.get(any(String.class))).willReturn(userEntity);
		given(userDao.save(any(UserEntity.class))).willReturn(userEntity);

		UserServiceImpl userService = new UserServiceImpl(userDao);
		userService.updatePassword(id, pwd, pwd);

		verify(userDao).get(any(String.class));
		verify(userDao).save(any(UserEntity.class));
	}

	@Test
	@DisplayName("사용자_비밀번호_변경_비밀번호_불일치")
	void updatePasswordFailure() {
		given(userDao.get(any(String.class))).willReturn(userEntity);

		UserServiceImpl userService = new UserServiceImpl(userDao);
		UserException exception = Assertions.assertThrows(UserException.class, () -> {
			userService.updatePassword(id, pwd + 1, pwd);
		});
		Assertions.assertEquals(exception.getMessage(), "비밀번호가 일치하지 않습니다.");

		verify(userDao).get(any(String.class));
	}

	@Test
	@DisplayName("사용자_위치정보_변경_성공")
	void updateLocationSuccess() {
		given(userDao.findByUserId(any(String.class))).willReturn(locationEntity);
		given(userDao.saveLocation(any(LocationEntity.class))).willReturn(locationEntity);

		UserServiceImpl userService = new UserServiceImpl(userDao);
		Assertions.assertEquals(userService.updateLocation(id, location), location);

		verify(userDao).findByUserId(any(String.class));
		verify(userDao).saveLocation(any(LocationEntity.class));
	}

	@Test
	@DisplayName("사용자_위치정보_변경_실패")
	void updateLocationFailure() {
		given(userDao.findByUserId(any(String.class))).willReturn(locationEntity);
		given(userDao.saveLocation(any(LocationEntity.class))).willReturn(null);

		UserServiceImpl userService = new UserServiceImpl(userDao);
		UserException exception = Assertions.assertThrows(UserException.class, () -> {
			userService.updateLocation(id, location);
		});
		Assertions.assertEquals(exception.getMessage(), "위치 변경에 실패했습니다.");

		verify(userDao).findByUserId(any(String.class));
		verify(userDao).saveLocation(any(LocationEntity.class));
	}

	@Test
	@DisplayName("사용자_위치정보_조회_성공")
	void getLocationSuccess() {
		given(userDao.findByUserId(any(String.class))).willReturn(locationEntity);

		UserServiceImpl userService = new UserServiceImpl(userDao);
		Assertions.assertEquals(userService.getLocation(id), location);

		verify(userDao).findByUserId(any(String.class));
	}

	@Test
	@DisplayName("사용자_위치정보_조회_실패")
	void getLocationFailure() {
		given(userDao.findByUserId(any(String.class))).willReturn(null);

		UserServiceImpl userService = new UserServiceImpl(userDao);
		Assertions.assertEquals(userService.getLocation(id), null);

		verify(userDao).findByUserId(any(String.class));
	}
}
