package com.tomato.market.service;

import com.tomato.market.data.dto.UserLoginDto;
import com.tomato.market.data.dto.UserSignUpDto;

public interface UserService {
	public UserSignUpDto registerUser(UserSignUpDto userSignUpDto);

	public UserLoginDto loginUser(UserLoginDto userLoginDto);
}
