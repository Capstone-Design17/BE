package com.tomato.market.data.dto;

import java.util.Date;

import com.tomato.market.data.entity.UserEntity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

	@Pattern(message = "잘못된 아이디 형식입니다.", regexp = "^[a-z0-9_-]{6,20}")
	private String id;

	@Pattern(message = "잘못된 비밀번호 형식입니다.", regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$ !%*#?&])"
		+ "[A-Za-z[0-9]$@$!%*#?&]{8,20}")
	private String pwd;

	// 비밀번호 확인
	@Pattern(message = "잘못된 비밀번호 형식입니다.", regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$ !%*#?&])"
		+ "[A-Za-z[0-9]$@$!%*#?&]{8,20}")
	private String pwdCheck;

	@NotBlank(message = "이름을 입력하세요.")
	@Pattern(regexp = "^[가-힣]{2,20}$")
	private String name;

	@NotBlank(message = "닉네임을 입력하세요.")
	@Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
	private String nickName;

	@NotBlank(message = "전화번호를 입력하세요.")
	@Pattern(regexp = "01[016789][^0][0-9]{2,3}[0-9]{4,4}", message = "유효하지 않은 전화번호 양식입니다.")
	private String phone;

	private Integer status;

	@NotNull(message = "생년월일을 입력하세요.")
	private Date birth;

	public static UserSignUpDto toUserSignUpDto(UserEntity userEntity) {
		return UserSignUpDto.builder().email(userEntity.getEmail()).id(userEntity.getId()).pwd(userEntity.getPwd())
			.name(userEntity.getName()).nickName(userEntity.getNickName()).phone(userEntity.getPhone())
			.status(userEntity.getStatus()).birth(userEntity.getBirth()).build();
	}

	public static UserEntity toUserEntity(UserSignUpDto userSignUpDto) {
		return UserEntity.builder().email(userSignUpDto.getEmail()).id(userSignUpDto.getId())
			.pwd(userSignUpDto.getPwd()).name(userSignUpDto.getName()).nickName(userSignUpDto.getNickName())
			.phone(userSignUpDto.getPhone()).status(userSignUpDto.getStatus()).birth(userSignUpDto.getBirth())
			.build();
	}
}
