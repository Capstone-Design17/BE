package com.tomato.market.data.dto;

import java.util.Date;

import com.tomato.market.data.entity.UserEntity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignUpDto {
	@NotEmpty(message = "이메일을 입력하세요.")
	@Email(message = "잘못된 이메일 형식입니다.")
	private String email;

	@Pattern(message = "잘못된 아이디 형식입니다.", regexp = "^[a-z0-9_-]{3,10}")
	private String id;

	@Pattern(message = "Invalid password format.", regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$ !%*#?&])"
		+ "[A-Za-z[0-9]$@$!%*#?&]{8,15}")
	private String pwd;

	@NotBlank(message = "이름을 입력하세요.")
	private String realName;

	@NotBlank(message = "닉네임을 입력하세요.")
	@Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
	private String nickName;

	@NotBlank(message = "전화번호를 입력하세요.")
	private String phone;

	private Integer status;

	//	@NotBlank(message = "생년월일을 입력하세요.")
	private Date birthday;

	public static UserSignUpDto toUserSignUpDto(UserEntity userEntity) {
		return UserSignUpDto.builder().email(userEntity.getEmail()).id(userEntity.getId()).pwd(userEntity.getPwd())
			.realName(userEntity.getRealName()).nickName(userEntity.getNickName()).phone(userEntity.getPhone())
			.status(userEntity.getStatus()).birthday(userEntity.getBirthday()).build();
	}

	public static UserEntity toUserEntity(UserSignUpDto userSignUpDto) {
		return UserEntity.builder().email(userSignUpDto.getEmail()).id(userSignUpDto.getId())
			.pwd(userSignUpDto.getPwd()).realName(userSignUpDto.getRealName()).nickName(userSignUpDto.getNickName())
			.phone(userSignUpDto.getPhone()).status(userSignUpDto.getStatus()).birthday(userSignUpDto.getBirthday())
			.build();
	}
}
