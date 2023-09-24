package com.tomato.market.data.dto;

import java.util.Date;

import com.tomato.market.data.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignUpDto {
	String email;
	String id;
	String pwd;
	String realName;
	String nickName;
	Integer phone;
	Integer status;
	Date birthday;

	public static UserSignUpDto toUserSignUpDto(UserEntity userEntity) {
		return UserSignUpDto.builder()
			.email(userEntity.getEmail())
			.id(userEntity.getId())
			.pwd(userEntity.getPwd())
			.realName(userEntity.getRealName())
			.nickName(userEntity.getNickName())
			.phone(userEntity.getPhone())
			.status(userEntity.getStatus())
			.birthday(userEntity.getBirthday())
			.build();
	}

	public static UserEntity toUserEntity(UserSignUpDto userSignUpDto) {
		return UserEntity.builder()
			.email(userSignUpDto.getEmail())
			.id(userSignUpDto.getId())
			.pwd(userSignUpDto.getPwd())
			.realName(userSignUpDto.getRealName())
			.nickName(userSignUpDto.getNickName())
			.phone(userSignUpDto.getPhone())
			.status(userSignUpDto.getStatus())
			.birthday(userSignUpDto.getBirthday())
			.build();
	}


	// 이메일, 핸드폰, 생년월일 추가 입력 필요


}
