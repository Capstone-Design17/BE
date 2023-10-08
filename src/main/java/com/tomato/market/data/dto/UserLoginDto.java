package com.tomato.market.data.dto;

import com.tomato.market.data.entity.UserEntity;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {
	@Pattern(message = "잘못된 아이디 형식입니다.", regexp = "^[a-z0-9_-]{6,20}")
	private String id;

	@Pattern(message = "잘못된 비밀번호 형식입니다.", regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$ !%*#?&])"
		+ "[A-Za-z[0-9]$@$!%*#?&]{8,20}")
	private String pwd;

	public static UserLoginDto toUserLoginDto(UserEntity userEntity) {
		return UserLoginDto.builder().id(userEntity.getId()).pwd(userEntity.getPwd()).build();
	}
}
