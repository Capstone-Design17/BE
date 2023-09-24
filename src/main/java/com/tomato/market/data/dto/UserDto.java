package com.tomato.market.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
	String id;
	String pwd;
	String realName;
	String nickName;
	// 이메일, 핸드폰, 생년월일 추가 입력 필요
}
